package ru.datamart.project.models;

public enum LimsStatusEnum {
    REJECTED {
        public String toString() {
            return "ОТКЛОНЕНО";
        }
    },
    APPROVED {
        public String toString() {
            return "ОДОБРЕНО";
        }
    };
}
