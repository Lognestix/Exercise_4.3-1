package ru.netology.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.domain.Issue;
import ru.netology.domain.OldestComparator;
import ru.netology.repository.IssueRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class IssueManagerTest {
    //Общие данные:
    private final IssueRepository repository = new IssueRepository();
    private final IssueManager issueManager = new IssueManager(repository);
    private final OldestComparator comparator = new OldestComparator();

    private final Issue zero = new Issue(2789, "List observable test engines and show their properties",
            true, "sormuras", "component: Platform, type: new feature",
            "sormuras", 11520);
    private final Issue first = new Issue(2788, "Make console launcher --classpath argument parser work like java's when referring to a jar directory",
            true, "mbucc", "component: Platform, status: waiting-for-feedback, theme: documentation, type: new feature",
            "No one assigned", 15_840);
    private final Issue second = new Issue(2787, "Extend ParallelExecutionConfiguration to specify a predicate for determing if a pool is saturated",
            true, "klease", "component: Platform, status: in progress, type: enhancement",
            "klease", 15_880);
    private final Issue third = new Issue(2779, "[reflection] ReflectionSupport to return Streams",
            true, "kriegfrj", "component: Jupiter, component: Platform, status: in progress, theme: extensions, theme: programming model, type: enhancement",
            "Attyuttam", 48_960);
    private final Issue fourth = new Issue(2778, "Using a single aggregator on the method level",
            false, "akruijff", "component: Jupiter, status: declined, theme: parameterized tests, theme: programming model",
            "No one assigned", 38_880);
    private final Issue fifth = new Issue(2775, "apiguardian-api version mismatch between source code and published POM",
            false, "e-ts", "theme: build",
            "No one assigned", 54_720);
    private final Issue sixth = new Issue(2774, "[FR] Mark tests preconditions / dependencies",
            false, "Hu1buerger", "component: Jupiter, status: duplicate, theme: programming model",
            "No one assigned", 56_160);
    private final Issue seventh = new Issue(2772, "Overview of shadowed dependencies in standalone JAR",
            false, "sormuras", "component: Platform, theme: documentation",
            "marcphilipp", 57_600);

    @BeforeEach
    public void setUp() {
        issueManager.add(zero);
        issueManager.add(first);
        issueManager.add(second);
        issueManager.add(third);
        issueManager.add(fourth);
        issueManager.add(fifth);
        issueManager.add(sixth);
        issueManager.add(seventh);
    }

    @Test   //Тест на отображения Issue по статусу - открыт(true) при сортировке newest
    public void shouldDisplayOpenWithCompareTo() {
        List<Issue> expected = List.of(zero, first, second, third);
        List<Issue> actual = (List<Issue>) issueManager.displayOpen();

        issueManager.sortBy(actual, Issue::compareTo);
        assertEquals(expected, actual, "Отображение Issue по статусу - открыт при сортировке newest");
        System.out.println(actual);
        System.out.println(expected);
    }

    @Test   //Тест на отображения Issue по статусу - открыт(true) при сортировке oldest
    public void shouldDisplayOpenWithComparator() {
        List<Issue> expected = List.of(third, second, first, zero);
        List<Issue> actual = (List<Issue>) issueManager.displayOpen();

        issueManager.sortBy(actual, comparator);
        assertEquals(expected, actual, "Отображение Issue по статусу - открыт при сортировке oldest");
    }

    @Test   //Тест на отображения Issue по статусу - открыт(true) при отсутсвии найденных элементов
    public void shouldDisplayOpenWithNotFoundValue() {
        List<Issue> expected = List.of();
        List<Issue> actual = (List<Issue>) issueManager.displayOpen();

        actual.removeAll(actual);
        assertEquals(expected, actual, "Отображение Issue по статусу - открыт при отсутсвии найденных элементов");
    }

    @Test   //Тест на отображения Issue по статусу - закрыт(false) при сортировке newest
    public void shouldDisplayCloseWithCompareTo() {
        List<Issue> expected = List.of(fourth, fifth, sixth, seventh);
        List<Issue> actual = (List<Issue>) issueManager.displayClose();

        issueManager.sortBy(actual, Issue::compareTo);
        assertEquals(expected, actual, "Отображение Issue по статусу - закрыт при сортировке newest");
    }

    @Test   //Тест на отображения Issue по статусу - закрыт(false) при сортировке oldest
    public void shouldDisplayCloseWithComparator() {
        List<Issue> expected = List.of(seventh, sixth, fifth, fourth);
        List<Issue> actual = (List<Issue>) issueManager.displayClose();

        issueManager.sortBy(actual, comparator);
        assertEquals(expected, actual, "Отображение Issue по статусу - закрыт при сортировке oldest");
    }

    @Test   //Тест на отображения Issue по статусу - закрыт(false) при отсутсвии найденных элементов
    public void shouldDisplayCloseWithNotFoundValue() {
        List<Issue> expected = List.of();
        List<Issue> actual = (List<Issue>) issueManager.displayClose();

        actual.removeAll(actual);
        assertEquals(expected, actual, "Отображение Issue по статусу - закрыт при отсутсвии найденных элементов");
    }
}