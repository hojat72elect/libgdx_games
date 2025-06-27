package com.riiablo.codec.excel;

import android.support.annotation.CallSuper;
import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;
import org.apache.commons.io.IOUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.ObjectIntMap;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.StreamUtils;

import com.riiablo.util.ClassUtils;

public abstract class Excel<T extends Excel.Entry> implements Iterable<T> {
  public static final String TAG = "Excel";

  private static final boolean DEBUG         = !true;
  private static final boolean DEBUG_COLS    = DEBUG && !true;
  private static final boolean DEBUG_COL_IDS = DEBUG && !true;
  private static final boolean DEBUG_IGNORED = DEBUG && !true;
  private static final boolean DEBUG_ENTRIES = DEBUG && !true;
  private static final boolean DEBUG_INDEXES = DEBUG && !true;
  private static final boolean DEBUG_KEY     = DEBUG && true;
  private static final boolean DEBUG_TYPE    = DEBUG && true;
  private static final boolean DEBUG_TIME    = DEBUG && true;
  private static final boolean DEBUG_BIN     = DEBUG && true;

  private static final boolean FORCE_PRIMARY_KEY = false;
  private static final boolean FORCE_TXT = !true;

  private static final ObjectSet EMPTY_SET = new ObjectSet<>();
  private static final ObjectIntMap EMPTY_MAP = new ObjectIntMap<>();

  public static boolean preserveOrder = false;

  public static final ObjectSet<String> EXPANSION = ObjectSet.with("Expansion");

  @SuppressWarnings("unchecked")
  public static <T> ObjectSet<T> emptySet() {
    return (ObjectSet<T>) EMPTY_SET;
  }

  @Target(ElementType.TYPE)
  @Retention(RetentionPolicy.RUNTIME)
  public @interface Index {}

  @Target(ElementType.TYPE)
  @Retention(RetentionPolicy.RUNTIME)
  public @interface Binned {}

  public static class Entry {
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Column {
      /** Used to index format */
      int     startIndex()  default 0;
      /** Used to index format */
      int     endIndex()    default 0;
      /** Format of column name, if not set, then field name is used */
      String  format()      default "";
      /** Used to index format not non-numerical indexes */
      String  values()[]    default {};
      /** Sets index of column of value (used for cases like weapons.txt where one column has no name) */
      int     columnIndex() default -1;
      /** Whether or not to read/write value in bin codec */
      boolean bin()         default true;
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Key {}
  }

  ObjectIntMap<String> STRING_TO_ID = EMPTY_MAP;
  IntMap<T> entries = new IntMap<>();
  Array<Entry> orderedEntries = new Array<>(Entry.class);

  public static <T extends Excel> T load(Class<T> excelClass, FileHandle txt) {
    return load(excelClass, txt, Excel.<String>emptySet());
  }

  public static <T extends Excel> T load(Class<T> excelClass, FileHandle txt, ObjectSet<String> ignore) {
    return load(excelClass, txt, null, ignore);
  }

  public static <T extends Excel> T load(Class<T> excelClass, FileHandle txt, FileHandle bin, ObjectSet<String> ignore) {
    try {
      if (ignore == null) ignore = emptySet();

      Class<Entry> entryClass = getEntryClass(excelClass);
      if (entryClass == null) throw new AssertionError(excelClass + " does not implement " + Entry.class);

      long start = System.currentTimeMillis();

      T excel;
      FileHandle file;
      if (!FORCE_TXT && isBinned(excelClass) && bin != null && bin.exists()) {
        if (DEBUG_TYPE) Gdx.app.debug(TAG, "Loading bin " + bin);
        excel = loadBin(bin, excelClass, entryClass);
        if (DEBUG_TIME) file = bin;
      } else {
        if (DEBUG_TYPE) Gdx.app.debug(TAG, "Loading txt " + txt);
        excel = loadTxt(txt, excelClass, entryClass, ignore);
        if (DEBUG_TIME) file = txt;
      }

      long end = System.currentTimeMillis();
      if (DEBUG_TIME) Gdx.app.debug(TAG, "Loaded " + file + " in " + (end - start) + "ms");

      excel.init();
      return excel;
    } catch (Throwable t) {
      throw new GdxRuntimeException("Couldn't load excel " + excelClass, t);
    }
  }

  @SuppressWarnings("unchecked")
  private static <T extends Excel> T loadTxt(FileHandle handle, Class<T> excelClass, Class<Entry> entryClass, ObjectSet<String> ignore) throws Exception {
    TxtParser in = null;
    try {
      in = TxtParser.loadFromFile(handle);
      return loadTxt(in, excelClass, entryClass, ignore);
    } catch (Throwable t) {
      throw new GdxRuntimeException("Couldn't load excel: " + handle, t);
    } finally {
      IOUtils.closeQuietly(in);
    }
  }

  private static <T extends Excel> T loadTxt(TxtParser in, Class<T> excelClass, Class<Entry> entryClass, ObjectSet<String> ignore) throws Exception {
    final boolean index = ClassUtils.hasAnnotation(entryClass, Index.class);

    Field primaryKey = null, firstKey = null;
    T excel = excelClass.newInstance();
    ObjectMap<Field, int[]> columns = new ObjectMap<>();
    String[] TMP = new String[1];
    for (Field field : entryClass.getFields()) {
      Entry.Column column = field.getAnnotation(Entry.Column.class);
      if (column == null) continue;

      Entry.Key key = field.getAnnotation(Entry.Key.class);
      if (key != null) {
        if (index) {
          Gdx.app.error(TAG, "primary key set in class annotated with " + Index.class);
        } else if (primaryKey != null) {
          boolean primaryDeclared = ClassUtils.isDeclaredField(entryClass, primaryKey);
          boolean fieldDeclared = ClassUtils.isDeclaredField(entryClass, field);
          if (primaryDeclared != fieldDeclared) {
            if (fieldDeclared) {
              if (DEBUG_KEY) Gdx.app.debug(TAG, "primary key " + primaryKey.getName() + " -> " + field.getName());
              primaryKey = field;
            }
          } else {
            Gdx.app.error(TAG, "more than one primary key for " + entryClass + " " + primaryKey.getName() + " and " + field.getName());
          }
        } else {
          primaryKey = field;
        }
      }

      if (firstKey == null) firstKey = field;

      final String format      = column.format();
      final String values[]    = column.values();
      final int    startIndex  = column.startIndex();
      final int    endIndex    = column.endIndex();
      final int    columnIndex = column.columnIndex();
      if (columnIndex >= 0) {
        columns.put(field, new int[] { columnIndex });
      } else if (format.isEmpty()) {
        final String fieldName = field.getName();
        if (values.length > 0) {
          String[] columnNames = new String[values.length];
          for (int i = 0; i < values.length; i++) {
            String name = values[i];
            if (DEBUG_COLS) Gdx.app.debug(TAG, name);
            columnNames[i] = name;
          }

          columns.put(field, in.getColumnId(columnNames));
        } else if (startIndex == 0 && endIndex == 0) {
          if (DEBUG_COLS) Gdx.app.debug(TAG, fieldName);
          TMP[0] = fieldName;
          columns.put(field, in.getColumnId(TMP));
        } else {
          String[] columnNames = new String[endIndex - startIndex];
          for (int i = startIndex, j = 0; i < endIndex; i++, j++) {
            String name = fieldName + i;
            if (DEBUG_COLS) Gdx.app.debug(TAG, name);
            columnNames[j] = name;
          }

          columns.put(field, in.getColumnId(columnNames));
        }
      } else {
        if (startIndex == 0 && endIndex == 0) {
          TMP[0] = format;
          columns.put(field, in.getColumnId(TMP));
        } else {
          // TODO: support custom replacement with field name %z or something
          String[] columnNames = new String[endIndex - startIndex];
          if (values.length == 0) {
            for (int i = startIndex, j = 0; i < endIndex; i++, j++) {
              String name = String.format(format, i);
              if (DEBUG_COLS) Gdx.app.debug(TAG, name);
              columnNames[j] = name;
            }
          } else {
            for (int i = 0; i < values.length; i++) {
              String name = String.format(format, values[i]);
              if (DEBUG_COLS) Gdx.app.debug(TAG, name);
              columnNames[i] = name;
            }
          }

          columns.put(field, in.getColumnId(columnNames));
        }
      }
    }

    if (primaryKey == null && !index) {
      if (FORCE_PRIMARY_KEY) {
        throw new IllegalStateException(entryClass + " does not have a " + Entry.Key.class + " set!");
      } else {
        primaryKey = firstKey;
        Gdx.app.error(TAG, entryClass + " does not have a " + Entry.Key.class + " set! Using " + firstKey.getName());
      }
    }

    if (DEBUG_COL_IDS) {
      for (ObjectMap.Entry<Field, int[]> entry : columns.entries()) {
        Gdx.app.debug(TAG, entry.key.getName() + ": " + Arrays.toString(entry.value));
      }
    }

    final int primaryKeyCol = index ? -1 : columns.get(primaryKey)[0];
    final Class primaryKeyType = index ? null : primaryKey.getType();
    for (int j = excel.offset(); in.nextLine() != null;) {
      Entry entry = entryClass.newInstance();

      String rowName = in.getString(0);
      if (ignore.contains(rowName)) {
        if (DEBUG_IGNORED) Gdx.app.debug(TAG, "Skipping row " + in.getIndex() + ", ignoring rows named " + rowName);
        continue;
      }

      String name = index ? null : in.getString(primaryKeyCol);
      for (ObjectMap.Entry<Field, int[]> row : columns.entries()) {
        Field field = row.key;
        int[] columnIds = row.value;
        Class type = field.getType();
        assert type.isArray() || columnIds.length == 1 : "field should only correspond to 1 column: " + field.getName() + ", " + columnIds.length + " columns (is it supposed to be an array?)";
        if (type == String.class) {
          String value = in.getString(columnIds[0]);
          field.set(entry, value);
          if (DEBUG_ENTRIES) Gdx.app.debug(TAG, String.format("Entry[%d](%s).%s=%s", j, name, field.getName(), value));
        } else if (type == String[].class) {
          String[] value = in.getString(columnIds);
          field.set(entry, value);
          if (DEBUG_ENTRIES) Gdx.app.debug(TAG, String.format("Entry[%d](%s).%s=%s", j, name, field.getName(), Arrays.toString(value)));
        } else if (type == byte.class) {
          byte value = in.getByte(columnIds[0]);
          field.setByte(entry, value);
          if (DEBUG_ENTRIES) Gdx.app.debug(TAG, String.format("Entry[%d](%s).%s=%s", j, name, field.getName(), value));
        } else if (type == byte[].class) {
          byte[] value = in.getByte(columnIds);
          field.set(entry, value);
          if (DEBUG_ENTRIES) Gdx.app.debug(TAG, String.format("Entry[%d](%s).%s=%s", j, name, field.getName(), Arrays.toString(value)));
        } else if (type == short.class) {
          short value = in.getShort(columnIds[0]);
          field.setShort(entry, value);
          if (DEBUG_ENTRIES) Gdx.app.debug(TAG, String.format("Entry[%d](%s).%s=%s", j, name, field.getName(), value));
        } else if (type == short[].class) {
          short[] value = in.getShort(columnIds);
          field.set(entry, value);
          if (DEBUG_ENTRIES) Gdx.app.debug(TAG, String.format("Entry[%d](%s).%s=%s", j, name, field.getName(), Arrays.toString(value)));
        } else if (type == int.class) {
          int value = in.getInt(columnIds[0]);
          field.setInt(entry, value);
          if (DEBUG_ENTRIES) Gdx.app.debug(TAG, String.format("Entry[%d](%s).%s=%s", j, name, field.getName(), value));
        } else if (type == int[].class) {
          int[] value = in.getInt(columnIds);
          field.set(entry, value);
          if (DEBUG_ENTRIES) Gdx.app.debug(TAG, String.format("Entry[%d](%s).%s=%s", j, name, field.getName(), Arrays.toString(value)));
        } else if (type == long.class) {
          long value = in.getLong(columnIds[0]);
          field.setLong(entry, value);
          if (DEBUG_ENTRIES) Gdx.app.debug(TAG, String.format("Entry[%d](%s).%s=%s", j, name, field.getName(), value));
        } else if (type == long[].class) {
          long[] value = in.getLong(columnIds);
          field.set(entry, value);
          if (DEBUG_ENTRIES) Gdx.app.debug(TAG, String.format("Entry[%d](%s).%s=%s", j, name, field.getName(), Arrays.toString(value)));
        } else if (type == boolean.class) {
          boolean value = in.getBoolean(columnIds[0]);
          field.setBoolean(entry, value);
          if (DEBUG_ENTRIES) Gdx.app.debug(TAG, String.format("Entry[%d](%s).%s=%s", j, name, field.getName(), value));
        } else if (type == boolean[].class) {
          boolean[] value = in.getBoolean(columnIds);
          field.set(entry, value);
          if (DEBUG_ENTRIES) Gdx.app.debug(TAG, String.format("Entry[%d](%s).%s=%s", j, name, field.getName(), Arrays.toString(value)));
        } else {
          throw new UnsupportedOperationException("No support for " + type + " fields");
        }
      }

      if (preserveOrder) excel.orderedEntries.add(entry);
      putIndex(primaryKey, primaryKeyType, j++, index, excel, entry);
    }

    return excel;
  }

  @SuppressWarnings("unchecked")
  private static <T extends Excel> T loadBin(FileHandle bin, Class<T> excelClass, Class<Entry> entryClass) throws Exception {
    final boolean index = ClassUtils.hasAnnotation(entryClass, Index.class);

    byte[] bytes = bin.readBytes();
    InputStream in = null;
    try {
      in = new ByteArrayInputStream(bytes);
      @SuppressWarnings("deprecation")
      com.riiablo.util.LittleEndianDataInputStream dis = new com.riiablo.util.LittleEndianDataInputStream(in);
      T excel = excelClass.newInstance();

      Field primaryKey = ClassUtils.findField(entryClass, Entry.Key.class);

      if (primaryKey == null && !index) {
        if (FORCE_PRIMARY_KEY) {
          throw new IllegalStateException(entryClass + " does not have a " + Entry.Key.class + " set!");
        } else {
          primaryKey = entryClass.getFields()[0];
          Gdx.app.error(TAG, entryClass + " does not have a " + Entry.Key.class + " set! Using " + primaryKey.getName());
        }
      }

      Class primaryKeyType = index ? null : primaryKey.getType();

      Class binClass = Class.forName(excelClass.getName() + "Bin");
      Method readBin = binClass.getMethod("readBin", entryClass, DataInput.class);

      int size = dis.readInt();
      if (DEBUG_BIN) Gdx.app.debug(TAG, "Reading " + size + " entries...");
      for (int i = 0, j = excel.offset(); i < size; i++, j++) {
        Entry entry = entryClass.newInstance();
        readBin.invoke(null, entry, dis);
        putIndex(primaryKey, primaryKeyType, j, index, excel, entry);
      }

      return excel;
    } finally {
      StreamUtils.closeQuietly(in);
    }
  }

  @SuppressWarnings("unchecked")
  private static <T extends Excel> void putIndex(Field primaryKey, Class primaryKeyType, int j, boolean indexed, T excel, Entry entry) throws Exception {
    if (indexed) {
      excel.put(j, entry);
    } else if (primaryKeyType == int.class) {
      int id = primaryKey.getInt(entry);
      excel.put(id, entry);
    } else if (primaryKeyType == String.class) {
      String id = (String) primaryKey.get(entry);
      excel.put(j, entry);

      if (excel.STRING_TO_ID == EMPTY_MAP) excel.STRING_TO_ID = new ObjectIntMap<>();
      if (!excel.STRING_TO_ID.containsKey(id)) excel.STRING_TO_ID.put(id, j);
    }
  }

  static boolean isBinned(Class c) {
    return ClassUtils.hasAnnotation(c, Binned.class);
  }

  public final boolean isBinned() {
    return isBinned(this.getClass());
  }

  public void readBin(DataInput in) throws IOException {}

  @SuppressWarnings("unchecked")
  public void writeBin(DataOutput out) throws IOException {
    //if (!isBinned()) return;
    Class excelClass = this.getClass();
    Class<T> entryClass = (Class<T>) getEntryClass(excelClass);
    String binClassName = excelClass.getName() + "Bin";
    try {
      Class binClass = Class.forName(binClassName);
      Method writeBin = binClass.getMethod("writeBin", entryClass, DataOutput.class);

      if (preserveOrder) {
        Entry[] items = orderedEntries.items;
        int size = orderedEntries.size;
        out.writeInt(size);
        for (int i = 0; i < size; i++) {
          Entry entry = items[i];
          writeBin.invoke(null, entry, out);
        }
      } else {
        int size = size();
        out.writeInt(size);
        for (Entry entry : this) {
          writeBin.invoke(null, entry, out);
        }
      }
    } catch (Throwable t) {
      throw new GdxRuntimeException("Failed to write bin for " + excelClass, t);
    }
  }

  @SuppressWarnings("unchecked")
  private static Class<Entry> getEntryClass(Class excelClass) {
    Class[] declaredClasses = excelClass.getDeclaredClasses();
    for (Class declaredClass : declaredClasses) {
      if (Entry.class.isAssignableFrom(declaredClass)) {
        return (Class<Entry>) declaredClass;
      }
    }

    return null;
  }

  @SuppressWarnings("unchecked")
  public Class<T> getEntryClass() {
    return (Class<T>) getEntryClass(getClass());
  }

  @CallSuper
  protected void put(int id, T value) {
    entries.put(id, value);
    if (DEBUG_INDEXES) Gdx.app.debug(TAG, id + ": " + value);
  }

  protected int offset() {
    return 0;
  }

  protected void init() {}

  public T get(String id) {
    return get(index(id));
  }

  public T get(int id) {
    T value = entries.get(id);
    if (DEBUG_INDEXES) Gdx.app.debug(TAG, id + " = " + value);
    return value;
  }

  public int index(String id) {
    return STRING_TO_ID.get(id, -1);
  }

  public int size() {
    return entries.size;
  }

  @Override
  public Iterator<T> iterator() {
    return entries.values().iterator();
  }
}
