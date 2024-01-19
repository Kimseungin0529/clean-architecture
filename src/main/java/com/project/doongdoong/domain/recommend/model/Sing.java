    package com.project.doongdoong.domain.recommend.model;

    import lombok.AllArgsConstructor;
    import lombok.Getter;

    @Getter
    @AllArgsConstructor
    public enum Sing {
        A("슬픔", "너를 보내고", "윤도현"); // 임시 처리

        private final String emotion;
        private final String songTitle;
        private final String singer;

    }
