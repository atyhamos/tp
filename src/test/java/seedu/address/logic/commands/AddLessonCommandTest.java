package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_LESSON_DAY_OF_WEEK_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_LESSON_END_TIME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_LESSON_HOURLY_RATE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_LESSON_START_TIME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_LESSON_SUBJECT_BOB;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showTuteeAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_TUTEE;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_TUTEE;
import static seedu.address.testutil.TypicalTutees.getTypicalTrackO;

import java.time.DayOfWeek;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.TrackO;
import seedu.address.model.UserPrefs;
import seedu.address.model.lesson.Lesson;
import seedu.address.model.lesson.Subject;
import seedu.address.model.lesson.Time;
import seedu.address.model.tutee.Tutee;
import seedu.address.testutil.TuteeBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code AddLessonCommand}.
 */
public class AddLessonCommandTest {

    private Model model = new ModelManager(getTypicalTrackO(), new UserPrefs());

    @Test
    public void execute_addLessonUnfilteredList_success() throws ParseException {
        Tutee firstTutee = model.getFilteredTuteeList().get(INDEX_FIRST_TUTEE.getZeroBased());
        Tutee editedTutee = new TuteeBuilder(firstTutee)
                .withLesson(LessonMock.getLesson())
                .build();

        AddLessonCommand addLessonCommand = AddLessonCommandParserMock.parse(INDEX_FIRST_TUTEE);

        String expectedMessage = String.format(AddLessonCommand.MESSAGE_SUCCESS, editedTutee);

        Model expectedModel = new ModelManager(new TrackO(model.getTrackO()), new UserPrefs());
        expectedModel.setTutee(firstTutee, editedTutee);

        assertCommandSuccess(addLessonCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() throws ParseException {
        showTuteeAtIndex(model, INDEX_FIRST_TUTEE);

        Tutee firstTutee = model.getFilteredTuteeList().get(INDEX_FIRST_TUTEE.getZeroBased());
        Tutee editedTutee = new TuteeBuilder(model.getFilteredTuteeList().get(INDEX_FIRST_TUTEE.getZeroBased()))
                .withLesson(LessonMock.getLesson()).build();

        AddLessonCommand addLessonCommand = AddLessonCommandParserMock.parse(INDEX_FIRST_TUTEE);

        String expectedMessage = String.format(AddLessonCommand.MESSAGE_SUCCESS, editedTutee);

        Model expectedModel = new ModelManager(new TrackO(model.getTrackO()), new UserPrefs());
        expectedModel.setTutee(firstTutee, editedTutee);

        assertCommandSuccess(addLessonCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidTuteeIndexUnfilteredList_failure() throws ParseException {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredTuteeList().size() + 1);
        AddLessonCommand addLessonCommand = AddLessonCommandParserMock.parse(outOfBoundIndex);

        assertCommandFailure(addLessonCommand, model, Messages.MESSAGE_INVALID_TUTEE_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of Track-O
     */
    @Test
    public void execute_invalidTuteeIndexFilteredList_failure() throws ParseException {
        showTuteeAtIndex(model, INDEX_FIRST_TUTEE);
        Index outOfBoundIndex = INDEX_SECOND_TUTEE;
        // ensures that outOfBoundIndex is still in bounds of Track-O list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getTrackO().getTuteeList().size());

        AddLessonCommand addLessonCommand = AddLessonCommandParserMock.parse(outOfBoundIndex);
        assertCommandFailure(addLessonCommand, model, Messages.MESSAGE_INVALID_TUTEE_DISPLAYED_INDEX);
    }

    @Test
    public void equals() throws ParseException {
        final AddLessonCommand standardCommand = AddLessonCommandParserMock.parse(INDEX_FIRST_TUTEE);

        // same values -> returns true
        AddLessonCommand commandWithSameValues = AddLessonCommandParserMock.parse(INDEX_FIRST_TUTEE);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(AddLessonCommandParserMock.parse(INDEX_SECOND_TUTEE)));
    }

    private static class AddLessonCommandParserMock {
        public static AddLessonCommand parse(Index index) throws ParseException {
            Subject subject = ParserUtil.parseSubject(VALID_LESSON_SUBJECT_BOB);
            DayOfWeek dayOfWeek = ParserUtil.parseDayOfWeek(VALID_LESSON_DAY_OF_WEEK_BOB);
            LocalTime startTime = ParserUtil.parseLocalTime(VALID_LESSON_START_TIME_BOB);
            LocalTime endTime = ParserUtil.parseLocalTime(VALID_LESSON_END_TIME_BOB);
            double hourlyRate = ParserUtil.parseHourlyRate(VALID_LESSON_HOURLY_RATE_BOB);

            return new AddLessonCommand(index, subject, dayOfWeek,
                    startTime, endTime, hourlyRate);
        }
    }

    private static class LessonMock {
        public static Lesson getLesson() throws ParseException {
            Subject subject = ParserUtil.parseSubject(VALID_LESSON_SUBJECT_BOB);
            DayOfWeek dayOfWeek = ParserUtil.parseDayOfWeek(VALID_LESSON_DAY_OF_WEEK_BOB);
            LocalTime startTime = ParserUtil.parseLocalTime(VALID_LESSON_START_TIME_BOB);
            LocalTime endTime = ParserUtil.parseLocalTime(VALID_LESSON_END_TIME_BOB);
            double hourlyRate = ParserUtil.parseHourlyRate(VALID_LESSON_HOURLY_RATE_BOB);

            return new Lesson(subject, new Time(dayOfWeek, startTime, endTime), hourlyRate);
        }
    }

}