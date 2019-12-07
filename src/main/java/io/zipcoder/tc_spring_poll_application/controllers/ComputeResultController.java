package io.zipcoder.tc_spring_poll_application.controllers;

import io.zipcoder.tc_spring_poll_application.domain.Option;
import io.zipcoder.tc_spring_poll_application.domain.Poll;
import io.zipcoder.tc_spring_poll_application.domain.Vote;
import io.zipcoder.tc_spring_poll_application.dtos.OptionCount;
import io.zipcoder.tc_spring_poll_application.dtos.VoteResult;
import io.zipcoder.tc_spring_poll_application.repositories.PollRepository;
import io.zipcoder.tc_spring_poll_application.repositories.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class ComputeResultController {

    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private PollRepository pollRepository;

    @GetMapping("/computeresult")
    public ResponseEntity<?> computeResult(@RequestParam Long pollId) {
        VoteResult voteResult = new VoteResult();
        Iterable<Vote> allVotes = voteRepository.findVotesByPoll(pollId);

        Poll poll = pollRepository.findOne(pollId);
        voteResult.setResults(new ArrayList<OptionCount>());
        for (Option o : poll.getOptions()) {
            OptionCount optionCount = new OptionCount();
            optionCount.setOptionId(o.getId());
            optionCount.setCount(Math.toIntExact(((ArrayList<Vote>) allVotes).stream().filter(v -> v.getOption().equals(o)).count()));
            voteResult.getResults().add(optionCount);
        }
        return new ResponseEntity<VoteResult>(voteResult, HttpStatus.OK);
    }
}