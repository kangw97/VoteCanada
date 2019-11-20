package ca.bcit.votecanada;

/**
 * interface for loading the poly line (routing) on map
  */

public interface TaskLoadedCallback {
    void onTaskDone(Object... values);
}
