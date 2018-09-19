package com.packt.sb5be.searchapp.dataservice.controller;

import com.packt.sb5be.searchapp.dataservice.repository.TopicRepository;
import com.packt.sb5be.searchapp.dataservice.dbmodel.orm.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class TopicController {
    @Autowired
    TopicRepository topicRepository;

    @GetMapping("/topics")
    public List<Topic> searchTopics(@RequestParam("searchString") String searchString) {
        //return topicRepository.findByDescriptionLikeIgnoreCase("%"+searchString+"%");
        return topicRepository.findByAttributeContainsText("description", searchString);
    }

    @GetMapping("/topic/{id}")
    public Optional<Topic> searchTopics(@PathVariable Long id) {
        return topicRepository.findById(id);
    }

    @PostMapping(value = "/topics")
    public ResponseEntity createTopic(@RequestBody Topic topic) {
        Topic newTopic = topicRepository.save(topic);
        return new ResponseEntity(newTopic, HttpStatus.OK);
    }

    @PatchMapping("/topic/{id}")
    public ResponseEntity patchTopic(@PathVariable Long id, @RequestBody Topic topic) {
        Topic updatedTopic = topicRepository.updateWith(topic, id);
        if (null == updatedTopic) {
            return new ResponseEntity("No topic found for ID " + id, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(updatedTopic, HttpStatus.OK);
    }

    @PutMapping("/topic/{id}")
    public ResponseEntity updateTopic(@PathVariable Long id, @RequestBody Topic topic) {
        Optional<Topic> topicToUpdate = topicRepository.findById(id);
        Topic updatedTopic;

        if (topicToUpdate.isPresent()) {
            updatedTopic = topicRepository.save(topic);
        } else {
            return new ResponseEntity("No topic found for ID " + id, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(updatedTopic, HttpStatus.OK);

    }

    @DeleteMapping("/topic/{id}")
    public ResponseEntity deleteTopic(@PathVariable Long id) {
        Optional<Topic> topic = topicRepository.findById(id);
        if (topic.isPresent()) {
            topicRepository.delete(topic.get());
        } else {
            return new ResponseEntity("No topic found for ID " + id, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(id, HttpStatus.OK);
    }
}
