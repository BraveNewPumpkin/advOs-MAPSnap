package Node;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapConfig {
    private final ThisNodeInfo thisNodeInfo;

    @Autowired
    public MapConfig(
            @Qualifier("Node/NodeConfigurator/thisNodeInfo")
            ThisNodeInfo thisNodeInfo
    ) {
        this.thisNodeInfo = thisNodeInfo;
    }

    @Bean
    @Qualifier("Node/LeaderElectionConfig/electingNewLeader")
    public GateLock electingNewLeader() {
        GateLock gateLock = new GateLock();
        gateLock.close();
        return gateLock;
    }

    @Bean
    @Qualifier("Node/SynchGhsConfig/sendingInitialMwoeSearchMessage")
    public GateLock sendingInitialMwoeSearchMessage() {
        GateLock sendingInitialMwoeSearchMessage = new GateLock();
        return sendingInitialMwoeSearchMessage;
    }

    @Bean
    @Qualifier("Node/SynchGhsConfig/mwoeSearchGate")
    public GateLock mwoeSearchGate() {
        GateLock mwoeSearchGate = new GateLock();
        return mwoeSearchGate;
    }
}
