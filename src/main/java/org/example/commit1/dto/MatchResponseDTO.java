package org.example.commit1.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MatchResponseDTO {

    private int matchId;
    private String status;
    private boolean waitingForPartner;
    private CommitInfo commitment;
    private BuddyInfo buddy;
    private boolean buddyCheckedInToday;
    private boolean[] myCheckins;
    private boolean[] buddyCheckins;

    @Data
    @Builder
    public static class CommitInfo {
        private int id;
        private String title;
        private String description;
        private String category;
    }

    @Data
    @Builder
    public static class BuddyInfo {
        private int id;
        private String name;
        private int streak;
    }
}