package com.kuddy.common.spot.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum District {
    DOBONG("10", "Dobong"),
    DONGDAEMUN("11", "Dongdaemun"),
    DONGJAK("12", "Dongjak"),
    EUNPYEONG("22", "Eunpyeong"),
    GANGBUK("3", "Gangbuk"),
    GANGDONG("2", "Gangdong"),
    GANGNAM("1", "Gangnam"),
    GANGSEO("4", "Gangseo"),
    GEUMCHEON("8", "Geumcheon"),
    GURO("7", "Guro"),
    GWANAK("5", "Gwanak"),
    GWANGJIN("6", "Gwangjin"),
    JONGNO("23", "Jongno"),
    JUNG("24", "Junggu"),
    JUNGNANG("25", "Jungnang"),
    MAPO("13", "Mapo"),
    NOWON("9", "Nowon"),
    SEOCHO("15", "Seocho"),
    SEODAEMUN("14", "Seodaemun"),
    SEONGBUK("17", "Seongbuk"),
    SEONGDONG("16", "Seongdong"),
    SONGPA("18", "Songpa"),
    YANGCHEON("19", "Yangcheon"),
    YEONGDEUNGPO("20", "Yeongdeungpo"),
    YONGSAN("21", "Yongsan");

    private final String code;
    private final String area;
}
