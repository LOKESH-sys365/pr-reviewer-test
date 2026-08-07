package com.example.AI_Powered.GitHub.PR.Reviewer;

import lombok.Data;

@Data
public class Filldiff {
    private String filename;
    private String patch;
    private String status;
    private int addition;
    private int deletion;

}
