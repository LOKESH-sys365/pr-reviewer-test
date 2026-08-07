package com.example.AI_Powered.GitHub.PR.Reviewer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.MessageDigest;

@RestController
@RequestMapping("/webhook")

public class Controller {
    @Value("${github.webhook.secret}")
    private  String secret;
    private final Pullrequestservice pullRequestService;

    public Controller(Pullrequestservice pullRequestService) {
        this.pullRequestService = pullRequestService;
    }



    @PostMapping("/github")
    public ResponseEntity<String> sendMessage(@RequestBody String payload, @RequestHeader("X-Hub-Signature-256") String signature) {
        if(!isValidSignature(payload,signature)){
            return ResponseEntity.status(401).body("Invalid signature");
        }
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root;
        try {
            root = mapper.readTree(payload);
        }catch (JsonProcessingException e){
            return ResponseEntity.badRequest().body("Invalid payload");

        }
        String action = root.path("action").asText();

        if("open".equals(action)||"synchronize".equals(action)){

            pullRequestService.analyzeAsync(root.path("repository").path("full_name").asText(),
                    root.path("number").asInt());
        }
        return ResponseEntity.ok("Message sent");

    }
    public boolean isValidSignature(String payload, String signature){
        if(signature==null || !signature.startsWith("sha256=")){
            return false;

        }
        String expected = "sha256=" + HmacUtils.hmacSha256Hex(secret, payload);
        return MessageDigest.isEqual(signature.getBytes(), expected.getBytes());
    }

}
