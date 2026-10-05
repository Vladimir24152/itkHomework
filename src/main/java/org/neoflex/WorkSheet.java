package org.neoflex;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class WorkSheet {
    String name;
    List<DayOfWeek> workDays;

    public WorkSheet(String name, List<DayOfWeek> workDays) {
        this.name = name;
        this.workDays = workDays;
    }

    public String getName() {
        return name;
    }

    public List<DayOfWeek> getWorkDays() {
        return workDays;
    }

    void test() {
        var denSheet = new WorkSheet("Денис", List.of(DayOfWeek.TUESDAY, DayOfWeek.THURSDAY, DayOfWeek.SUNDAY));
        var benSheet = new WorkSheet("Бен", List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.SATURDAY));
        var lisSheet = new WorkSheet("Лиза", List.of(DayOfWeek.THURSDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY));
        var sheets = List.of(denSheet, benSheet, lisSheet);
        // TODO: Необходимо вывести на экран дни в которые работало более 1 человека

         sheets.stream()
                 .flatMap(workSheet -> workSheet.getWorkDays().stream().distinct())
                 .collect(Collectors.groupingBy(Function.identity(),Collectors.counting()))
                 .entrySet().stream()
                 .filter(dayCountEntry -> dayCountEntry.getValue() > 1)
                 .map(Map.Entry::getKey)
            .forEach(System.out::println);
    }
}
