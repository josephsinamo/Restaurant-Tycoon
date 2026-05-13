package event;

public interface GameEvent {
    void execute();
    String getEventName();
}