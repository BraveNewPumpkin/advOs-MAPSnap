package Node;

import java.util.ArrayList;
import java.util.List;

public class NodeIncrementableRoundSynchronizer<T extends RoundSynchronizable> extends NodeMessageRoundSynchronizer<T> {
    public final List<Integer> roundProgress;

    public NodeIncrementableRoundSynchronizer(int roundSize) {
        super(roundSize);
        System.out.println("SRS Round Size: "+ roundSize);
        roundProgress = new ArrayList<>();
    }

    @Override
    public void enqueueMessage(T message) {
        int messageRoundNumber = message.getRoundNumber();
        int currentRoundIndex = roundProgress.size() - 1;
        System.out.println(" SRSync : Current Rounde Index: "+ currentRoundIndex);
        if(currentRoundIndex != messageRoundNumber){
            for(int i = currentRoundIndex; i <= messageRoundNumber; i++) {
                roundProgress.add(0);
            }
        }
        //2 steps because Integer is immutable
        int messageRoundProgress = roundProgress.get(messageRoundNumber);
        messageRoundProgress++;
        roundProgress.set(messageRoundNumber, messageRoundProgress);

        super.enqueueMessage(message);
    }

    public void incrementProgressForRound(int roundNumber) {
        int selectedRoundProgress = 0;
        if(roundProgress.size() >= roundNumber) {
            selectedRoundProgress = roundProgress.get(roundNumber);
            selectedRoundProgress++;
        }
        roundProgress.set(roundNumber, selectedRoundProgress);
        ensureQueueForRoundIsInitialized(roundNumber);
    }

    public synchronized void incrementProgressAndRunIfReady(int roundNumber, Runnable work) {
        incrementProgressForRound(roundNumber);
        //only try to run if the round we're progressing is current round
        if(getRoundNumber() == roundNumber) {
            runIfReady(work);
        }
    }

    @Override
    public void runIfReady(Runnable work) {
        System.out.println("SRS run if ready");
        int progressSoFarThisRound = roundProgress.get(getRoundNumber());

        if (progressSoFarThisRound == getRoundSize()) {
            work.run();
        }
    }
}
