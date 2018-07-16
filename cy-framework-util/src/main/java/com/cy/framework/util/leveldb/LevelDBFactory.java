package com.cy.framework.util.leveldb;

public class LevelDBFactory {
    private static LevelDBFactory levelDBFactory = null;

    public LevelDBFactory getInstance() {
        if (levelDBFactory == null) {
            levelDBFactory = new LevelDBFactory();
        }
        return levelDBFactory;
    }

    private LevelDBFactory() {
    }
}
