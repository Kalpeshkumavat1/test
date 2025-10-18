Java Design Patterns - GUI Demos

Folders and packages
behavioural/ → package behavioural
structural/ → package structural
creational/ → package creational

Prerequisites
- Java JDK 8+ on PATH (javac, java)

Clean and compile (from the project root)
1) Remove stale classes (optional but recommended on Windows PowerShell):
   powershell -NoProfile -Command "Get-ChildItem -Recurse -Filter *.class | Remove-Item -Force"

2) Compile all sources and output classes into correct package folders:
   javac -d . behavioural/*.java structural/*.java creational/*.java

Run
Run from the project root using fully qualified class names (because files declare packages):

Behavioral (Observer & Strategy):
- Course Observer GUI:
  java behavioural.CourseObserverGUI
- Strategy Quiz GUI:
  java behavioural.StrategyQuizDemo

Structural (Composite & Decorator):
- Composite Course GUI:
  java structural.CompositeCourseDemo
- Decorator Lesson GUI:
  java structural.DecoratorLessonDemo

Creational (Factory & Singleton):
- Factory Content GUI:
  java creational.FactoryContentDemo
- Singleton Catalog GUI:
  java creational.SingletonCatalogDemo

Troubleshooting
- Error: Could not find or load main class X or wrong name Y (package/Name)
  Cause: Running from wrong folder or compiled without -d .
  Fix: Recompile with: javac -d . behavioural/*.java structural/*.java creational/*.java
       Then run with the fully qualified name from the project root.

- Ambiguous List errors (java.util.List vs java.awt.List)
  Fix: Code updated to use java.util.List explicitly. If you change code, prefer fully qualifying java.util.List in GUI files that import java.awt.*.

- If GUIs don't appear
  Some shells buffer outputs for GUI apps. The app window should appear; if not, ensure no modal dialogs are hidden behind other windows.


