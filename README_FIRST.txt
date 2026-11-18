PLAGIACHECK 2.0
Requires JDK 17+ and Maven.
Open this folder in VS Code, then run:
  mvn clean compile
  mvn exec:java -Dexec.mainClass="app.Main"
First run requires internet to download dependencies.
Supports TXT, selectable-text PDF and DOCX. Scanned PDFs require OCR and are not supported by this starter.
Similarity is an indicator, not proof of plagiarism.
Back up your existing project before replacing it.
