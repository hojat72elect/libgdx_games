package com.riiablo.codec.excel;

@Excel.Binned
public class LowQualityItems extends Excel<LowQualityItems.Entry> {
  @Excel.Index
  public static class Entry extends Excel.Entry {
    @Override
    public String toString() {
      return Name;
    }

    @Column
    public String Name;
  }
}
