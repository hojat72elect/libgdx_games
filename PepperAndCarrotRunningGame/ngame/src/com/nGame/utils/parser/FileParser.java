package com.nGame.utils.parser;

import com.badlogic.gdx.Gdx;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StreamTokenizer;

public abstract class FileParser {
    protected StreamTokenizer tokenizer;

    public FileParser(String filename) throws FileNotFoundException {
        tokenizer = new StreamTokenizer(Gdx.files.internal(filename).reader());
        //Tokenzizer defaults
        tokenizer.commentChar('#');
        tokenizer.quoteChar('"');


    }

    public void setCommentChar(Character ch) {
        tokenizer.commentChar(ch);
    }

    public void setQuoteChar(Character ch) {
        tokenizer.quoteChar(ch);
    }

    public void parse() throws IOException {
        int token;
        while ((token = tokenizer.nextToken()) != StreamTokenizer.TT_EOF) {
            parseToken(token);
        }
    }

    protected abstract void parseToken(int ch) throws IOException;

}
