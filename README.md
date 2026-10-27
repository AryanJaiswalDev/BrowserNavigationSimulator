# Browser Navigation Simulator (Java)

Data structure used: **Stack** (own linked-list implementation)

## Folder structure
```
BrowserNavigationSimulator/
├── src/
│   ├── Main.java                 -> starts the app
│   ├── ds/Stack.java             -> custom Stack (push, pop, peek)
│   ├── model/BrowserHistory.java -> back/forward logic using 2 stacks
│   └── ui/                       -> Swing frontend
│       ├── BrowserFrame.java     -> main window
│       ├── PagePanel.java        -> fake web page preview
│       ├── StackView.java        -> draws a stack as cards
│       ├── RoundedButton.java    -> styled button
│       └── Theme.java            -> colors and fonts
└── README.md
```

## Run
```
cd BrowserNavigationSimulator
javac -d out src/Main.java src/ds/*.java src/model/*.java src/ui/*.java
java -cp out Main
```

## How it works
- Visit a page: current page goes onto the Back stack, Forward stack is cleared.
- Back: current page goes onto the Forward stack, top of Back stack becomes current.
- Forward: current page goes onto the Back stack, top of Forward stack becomes current.
