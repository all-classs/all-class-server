//package org.classreviewsite.lecture.service;
//
//import lombok.RequiredArgsConstructor;
//import org.classreviewsite.lecture.infrastructure.ImageUrl;
//import org.classreviewsite.review.service.ImageUrlService;
//import org.classreviewsite.lecture.infrastructure.Professor;
//import org.classreviewsite.lecture.infrastructure.ProfessorDataRepository;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.NoSuchElementException;
//
//@Service
//@RequiredArgsConstructor
//public class ProfessorService {
//
//    private final ProfessorDataRepository professorDataRepository;
//    private final ImageUrlService imageUrlService;
//
//    @Transactional(readOnly = true)
//    public Professor findByName(String name){
//        return professorDataRepository.findByProfessorName(name).orElseThrow(() -> new NoSuchElementException("해당 교수가 없습니다."));
//    }
//
//    @Transactional
//    public void updateProfile(Long imageId, String name){
//        ImageUrl imageUrl =  imageUrlService.findById(imageId);
//        Professor professor = findByName(name);
//
//        professor.updateProfile(imageUrl);
//    }
//
//
//}
