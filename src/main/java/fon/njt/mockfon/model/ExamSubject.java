package fon.njt.mockfon.model;

public enum ExamSubject {
    MATH("Matematika"),
    GENERAL_KNOWLEDGE("OpstaZnanja");

    private String displayName;

    ExamSubject(String displayName) {
        this.displayName = displayName;
    }

    public static ExamSubject fromDisplayName(String displayName) {
        for (ExamSubject es : ExamSubject.values()) {
            if (es.displayName.equalsIgnoreCase(displayName)) {
                return es;
            }
        }
        throw new IllegalArgumentException("Unknown ExamSubject: " + displayName);
    }

    @Override
    public String toString() {
        return displayName;
    }
}

