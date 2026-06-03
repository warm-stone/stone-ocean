package com.example.stoneocean.entity.fishing;

import lombok.Getter;

@Getter
public enum GameType {
    GUESSING("猜谜"),
    CAIYAN_BAIKE("猜盐-baike");

    private final String desc;

    GameType( String desc) { // 构造方法私有化（枚举默认私有）
        this.desc = desc;
    }


}
