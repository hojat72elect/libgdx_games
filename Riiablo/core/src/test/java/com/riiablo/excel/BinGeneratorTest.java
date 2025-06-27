package com.riiablo.excel;

import org.junit.jupiter.api.*;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import com.riiablo.RiiabloTest;
import com.riiablo.excel.txt.MonStats;
import com.riiablo.logger.Level;
import com.riiablo.logger.LogManager;

public class BinGeneratorTest extends RiiabloTest {
  @BeforeAll
  public static void before() {
    LogManager.setLevel("com.riiablo.excel.BinGenerator", Level.TRACE);
  }

  @Test
  @SuppressWarnings("unchecked")
  public void monstats() throws IOException {
    FileHandle handle = Gdx.files.internal("test/monstats.txt");
    Excel monstats = Excel.loadTxt(new MonStats(), handle);

    BinGenerator generator = new BinGenerator();
    generator.binDir = Gdx.files.absolute(
        "C:\\Users\\csmith\\projects\\libgdx\\riiablo\\assets");

    FileHandle excelDir = generator.binDir.child("data/global/excel2");
    excelDir.mkdirs();

    generator.generateBin(excelDir, monstats);
  }
}
