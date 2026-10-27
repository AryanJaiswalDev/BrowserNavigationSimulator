package model;

import ds.Stack;

/**
 * Browser history logic using two stacks:
 *  - backStack    : pages we can go BACK to
 *  - forwardStack : pages we can go FORWARD to
 */
public class BrowserHistory {

    private final Stack<String> backStack = new Stack<>();
    private final Stack<String> forwardStack = new Stack<>();
    private String current = "about:blank";

    // User opens a new page
    public void visit(String url) {
        backStack.push(current);   // current page goes to back history
        forwardStack.clear();      // new visit erases forward history
        current = url;
    }

    // Back button
    public boolean back() {
        if (backStack.isEmpty()) return false;
        forwardStack.push(current);
        current = backStack.pop();
        return true;
    }

    // Forward button
    public boolean forward() {
        if (forwardStack.isEmpty()) return false;
        backStack.push(current);
        current = forwardStack.pop();
        return true;
    }

    public String getCurrent() {
        return current;
    }

    public boolean canGoBack() {
        return !backStack.isEmpty();
    }

    public boolean canGoForward() {
        return !forwardStack.isEmpty();
    }

    public Stack<String> getBackStack() {
        return backStack;
    }

    public Stack<String> getForwardStack() {
        return forwardStack;
    }
}
