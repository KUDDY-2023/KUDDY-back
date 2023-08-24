package com.kuddy.common.spot.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum District {
    DOBONG("10", "Dobong-gu"),
    DONGDAEMUN("11", "Dongdaemun-gu"),
    DONGJAK("12", "Dongjak-gu"),
    EUNPYEONG("22", "Eunpyeong-gu"),
    GANGBUK("3", "Gangbuk-gu"),
    GANGDONG("2", "Gangdong-gu"),
    GANGNAM("1", "Gangnam-gu"),
    GANGSEO("4", "Gangseo-gu"),
    GEUMCHEON("8", "Geumcheon-gu"),
    GURO("7", "Guro-gu"),
    GWANAK("5", "Gwanak-gu"),
    GWANGJIN("6", "Gwangjin-gu"),
    JONGNO("23", "Jongno-gu"),
    JUNG("24", "Jung-gu"),
    JUNGNANG("25", "Jungnang-gu"),
    MAPO("13", "Mapo-gu"),
    NOWON("9", "Nowon-gu"),
    SEOCHO("15", "Seocho-gu"),
    SEODAEMUN("14", "Seodaemun-gu"),
    SEONGBUK("17", "Seongbuk-gu"),
    SEONGDONG("16", "Seongdong-gu"),
    SONGPA("18", "Songpa-gu"),
    YANGCHEON("19", "Yangcheon-gu"),
    YEONGDEUNGPO("20", "Yeongdeungpo-gu"),
    YONGSAN("21", "Yongsan-gu");

    private final String code;
    private final String area;
}
