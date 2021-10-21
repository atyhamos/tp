package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.storage.JsonAdaptedTutee.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalTutees.BENSON;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.tutee.Address;
import seedu.address.model.tutee.Level;
import seedu.address.model.tutee.Name;
import seedu.address.model.tutee.Phone;

public class JsonAdaptedTuteeTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_LEVEL = "w5";
    private static final String INVALID_TAG = "#friend";
    private static final String INVALID_LESSON = "{\r\n  \"subject\" : {\r\n    \"value\" : \"Ec@ns\"\r\n  },\r\n  "
            + "\"time\" : {\r\n    \"dayOfOccurrence\" : \"Moon day\",\r\n    \"startTime\" : \"23:30\",\r\n    "
            + "\"endTime\" : \"25:30\",\r\n    \"duration\" : 2.0\r\n  },\r\n  \"hourlyRate\" : 40.5,\r\n  \"cost\" : "
            + "81.0\r\n}";

    private static final String VALID_NAME = BENSON.getName().toString();
    private static final String VALID_PHONE = BENSON.getPhone().toString();
    private static final String VALID_LEVEL = BENSON.getLevel().value;
    private static final String VALID_ADDRESS = BENSON.getAddress().toString();
    private static final String VALID_REMARK = BENSON.getRemark().toString();
    private static final List<JsonAdaptedTag> VALID_TAGS = BENSON.getTags().stream()
            .map(JsonAdaptedTag::new)
            .collect(Collectors.toList());
    private static final List<String> VALID_LESSONS = new ArrayList<>();

    @BeforeAll
    public static void getLessons() throws JsonProcessingException {
        Set<Lesson> bensonLessons = BENSON.getLessons();
        for (Lesson lesson : bensonLessons) {
            VALID_LESSONS.add(JsonUtil.toJsonString(lesson));
        }
    }

    @Test
    public void toModelType_validTuteeDetails_returnsTutee() throws Exception {
        JsonAdaptedTutee tutee = new JsonAdaptedTutee(BENSON);
        assertEquals(BENSON, tutee.toModelType());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedTutee tutee =
                new JsonAdaptedTutee(INVALID_NAME, VALID_PHONE, VALID_LEVEL, VALID_ADDRESS,
                        VALID_REMARK, VALID_TAGS, VALID_LESSONS);

        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, tutee::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedTutee tutee = new JsonAdaptedTutee(null, VALID_PHONE, VALID_LEVEL, VALID_ADDRESS,
                VALID_REMARK, VALID_TAGS, VALID_LESSONS);

        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, tutee::toModelType);
    }

    @Test
    public void toModelType_invalidPhone_throwsIllegalValueException() {
        JsonAdaptedTutee tutee =
                new JsonAdaptedTutee(VALID_NAME, INVALID_PHONE, VALID_LEVEL, VALID_ADDRESS, VALID_REMARK,
                        VALID_TAGS, VALID_LESSONS);

        String expectedMessage = Phone.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, tutee::toModelType);
    }

    @Test
    public void toModelType_nullPhone_throwsIllegalValueException() {
        JsonAdaptedTutee tutee = new JsonAdaptedTutee(VALID_NAME, null, VALID_LEVEL, VALID_ADDRESS,
                VALID_REMARK, VALID_TAGS, VALID_LESSONS);

        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, tutee::toModelType);
    }

    @Test
    public void toModelType_invalidLevel_throwsIllegalValueException() {
        JsonAdaptedTutee tutee =
                new JsonAdaptedTutee(VALID_NAME, VALID_PHONE, INVALID_LEVEL, VALID_ADDRESS, VALID_REMARK,
                        VALID_TAGS, VALID_LESSONS);

        String expectedMessage = Level.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, tutee::toModelType);
    }

    @Test
    public void toModelType_nullLevel_throwsIllegalValueException() {
        JsonAdaptedTutee tutee = new JsonAdaptedTutee(VALID_NAME, VALID_PHONE, null, VALID_ADDRESS,
                VALID_REMARK, VALID_TAGS, VALID_LESSONS);

        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Level.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, tutee::toModelType);
    }

    @Test
    public void toModelType_invalidAddress_throwsIllegalValueException() {
        JsonAdaptedTutee tutee =
                new JsonAdaptedTutee(VALID_NAME, VALID_PHONE, VALID_LEVEL, INVALID_ADDRESS,
                        VALID_REMARK, VALID_TAGS, VALID_LESSONS);

        String expectedMessage = Address.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, tutee::toModelType);
    }

    @Test
    public void toModelType_nullAddress_throwsIllegalValueException() {
        JsonAdaptedTutee tutee = new JsonAdaptedTutee(VALID_NAME, VALID_PHONE, VALID_LEVEL, null,
                VALID_REMARK, VALID_TAGS, VALID_LESSONS);

        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, tutee::toModelType);
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<JsonAdaptedTag> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(new JsonAdaptedTag(INVALID_TAG));

        JsonAdaptedTutee tutee =
                new JsonAdaptedTutee(VALID_NAME, VALID_PHONE, VALID_LEVEL, VALID_ADDRESS,
                        VALID_REMARK, invalidTags, VALID_LESSONS);
        assertThrows(IllegalValueException.class, tutee::toModelType);
    }

    @Test
    public void toModelType_invalidLessons_throwsIoException() {
        List<String> invalidLessons = new ArrayList<>(VALID_LESSONS);
        invalidLessons.add(INVALID_LESSON);
        JsonAdaptedTutee person =
                new JsonAdaptedTutee(VALID_NAME, VALID_PHONE, VALID_LEVEL, VALID_ADDRESS,
                        VALID_REMARK, VALID_TAGS, invalidLessons);
        assertThrows(IOException.class, person::toModelType);
    }
}