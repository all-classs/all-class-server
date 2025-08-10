//package org.classreviewsite.lecture.infrastructure;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
///**
// * deprecated
// */
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//@Getter
//@Entity
//@Table(name = "ClassList")
//public class ClassList {
//    @Id
//    @Column(nullable = false, length = 20, unique = true)
//    private Long classNumber;
//
//    @Column(columnDefinition = "TEXT", nullable = true, length = 255, unique = false)
//    private String classIntroduction;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "lecId", nullable = false)
//    private Lecture lecture;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "professorId")
//    private Professor professor;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "icon", nullable = false)
//    private ImageUrl icon;
//
//    @Column(nullable = false)
//    private boolean opened;
//}