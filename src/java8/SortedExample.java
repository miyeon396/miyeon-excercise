package java8;

import java.util.Comparator;
import java.util.stream.Stream;

public class SortedExample {

    public static void main(String[] args) {
        Stream<Student> studentStream = Stream.of(
                new Student("이자바", 3, 300),
                new Student("김자바", 1, 200),
                new Student("연자바", 2, 100),
                new Student("노자바", 2, 150),
                new Student("박자바", 1, 200),
                new Student("소자바", 3, 180),
                new Student("나자바", 3, 180)
        );

        System.out.println("1. 학생 스트림을 반별 성적순, 그리고 이름순으로 정렬\n");
        studentStream.sorted(Comparator.comparing(Student::getBan)
                        .thenComparing(Student::getTotalScore)
                        .thenComparing(Student::getName))
                .forEach(System.out::println);

        System.out.println("\n\n");

        Stream<Student> studentStream2 = Stream.of(
                new Student("이자바", 3, 300),
                new Student("김자바", 1, 200),
                new Student("연자바", 2, 100),
                new Student("노자바", 2, 150),
                new Student("박자바", 1, 200),
                new Student("소자바", 3, 180),
                new Student("나자바", 3, 180)
        );

        System.out.println("2. 학생의 성적을 반별 오름차순, 총점별 내림차순으로 정렬\n");
        studentStream2.sorted(Comparator.comparing(Student::getBan)
                        .thenComparing(Comparator.naturalOrder()))
                .forEach(System.out::println);
    }

}

class Student implements Comparable<Student> {
    String name;
    int ban;
    int totalScore;
    Student(String name, int ban, int totalScore) {
        this.name = name;
        this.ban = ban;
        this.totalScore = totalScore;
    }

    public String toString() {
        return String.format("[%s, %d, %d]", name, ban, totalScore);
    }

    String getName() {
        return name;
    }
    int getBan() {
        return ban;
    }
    int getTotalScore() {
        return totalScore;
    }

    @Override
    public int compareTo(Student o) {
        return o.totalScore - this.totalScore; //총점desc
    }
}
