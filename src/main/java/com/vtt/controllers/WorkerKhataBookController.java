package com.vtt.controllers;

import com.vtt.dtoforSrc.KhataBookRequestDTO;
import com.vtt.dtoforSrc.LabelPaymentRequestDTO;
import com.vtt.entities.LabelGenerated;
import com.vtt.entities.User;
import com.vtt.entities.WorkerKhataBook;
import com.vtt.repository.LabelGeneratedRepository;
import com.vtt.repository.UserRepository;
import com.vtt.repository.WorkerKhataBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/khata")
public class WorkerKhataBookController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WorkerKhataBookRepository khataBookRepository;

    @Autowired
    private LabelGeneratedRepository labelGeneratedRepository;


    @GetMapping("/last-balances")
    public List<Map<String, Object>> getLastBalancesForAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(user -> khataBookRepository.findTopByUserOrderByCreatedAtDesc(user))
                .filter(tx -> tx != null)
                .map(tx -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("userId", tx.getUser().getUserId());

                    map.put("balance", tx.getBalance());
                    return map;
                })
                .toList();
    }

    // POST API to add transaction
    @PostMapping("/transaction")
    public String addTransaction(@RequestBody KhataBookRequestDTO request) {
        User user = userRepository.findByUserId(request.getUserId());

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        // Get last transaction to fetch old balance
//        WorkerKhataBook lastTransaction = khataBookRepository.findTopByUserOrderByCreatedAtDesc(user);
        double oldBalance = (user.getBalance() != null) ? user.getBalance() : 0.0;
        double newBalance;

        // Calculate new balance based on type (credit/debit)
        if ("credit".equalsIgnoreCase(request.getType())) {
            newBalance =oldBalance  + request.getAmount();
        } else if ("debit".equalsIgnoreCase(request.getType())) {
            newBalance = oldBalance - request.getAmount();
        } else {
            throw new RuntimeException("Invalid transaction type: " + request.getType());
        }

        // Create new transaction with updated balance
        WorkerKhataBook khata = new WorkerKhataBook();
        khata.setUser(user);
        khata.setAmount(request.getAmount());
        khata.setType(request.getType());
        khata.setNote(request.getNote());
        khata.setDate(request.getDate());
        khata.setBalance(newBalance);
        user.setBalance(newBalance);
        userRepository.save(user);
        khataBookRepository.save(khata);

        return "Transaction saved successfully.";
    }



    // GET API to fetch all transactions for a user
    @GetMapping("/{userId}")
    public List<WorkerKhataBook> getTransactionsByUser(@PathVariable String userId) {
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        return khataBookRepository.findByUser(user);
    }

    @PostMapping("/label-payments")
    public String processLabelPayments(@RequestBody LabelPaymentRequestDTO request) {
        // Validate user exists
        System.out.println("eryrrtyhrtuyhrujytjikmm,5-----------------");
        User user = userRepository.findByUserId(request.getUserId());
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        System.out.println("eryrrtyhrtuyhrujytjikmm,4-----------------");
        // Process each label in the list
        for (LabelPaymentRequestDTO.LabelPaymentDetail labelDetail : request.getLabels()) {
            System.out.println("eryrrtyhrtuyhrujytjikmm,3----------------");
            // Find the label by labelNumber
            Optional<LabelGenerated> optionalLabel = labelGeneratedRepository.findByLabelNumber(labelDetail.getLabelNumber());
            if (!optionalLabel.isPresent()) {
                throw new RuntimeException("Label not found: " + labelDetail.getLabelNumber());
            }

            LabelGenerated label = optionalLabel.get();
            System.out.println("eryrrtyhrtuyhrujytjikmm,2-----------------"+label);
            // Update the paid status for the specific user in the label
            label.getUsers().stream()
                    .filter(userWork -> userWork.getUser().getUserId().equals(request.getUserId()))
                    .findFirst()
                    .ifPresent(userWork -> {
                        userWork.setPaid(labelDetail.isPaid());
                    });
            System.out.println("eryrrtyhrtuyhrujytjikmm,-----------------");
            // Save the updated label
            labelGeneratedRepository.save(label);
        }


        //
        double oldBalance = (user.getBalance() != null) ? user.getBalance() : 0.0;
        double newBalance;
        newBalance = oldBalance + request.getTotalAmount();


        // Create a new khata book entry
        WorkerKhataBook khataEntry = new WorkerKhataBook();
        khataEntry.setUser(user);
        khataEntry.setAmount(request.getTotalAmount());
        khataEntry.setType("payment");
        khataEntry.setNote(request.getNotes());
        khataEntry.setDate(request.getDate());
        khataEntry.setType("credit");
        khataEntry.setBalance(newBalance);
        user.setBalance(newBalance);
        userRepository.save(user);
        khataBookRepository.save(khataEntry);

        return "Label payments processed and khata entry created successfully";
    }
}