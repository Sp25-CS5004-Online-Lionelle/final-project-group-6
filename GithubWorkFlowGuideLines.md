## **GitHub Workflow GuideLine for Final Project**  

#### **1. Clone the Repository**  (done during team meeting)
 clone the repository to your local machine:  
```bash
git clone <repository-url>
cd <repository-name>
```

#### **2. Create a New Branch**  
Before making changes, create a new branch:  
```bash
git checkout -b <feature-branch-name>
```

#### **3. Make Changes & Commit**  
Edit files, then stage and commit your changes:  
```bash
git add .
git commit -m "Describe your changes here"
```

#### **4. Push Your Branch**  
Upload your branch to GitHub:  
```bash
git push origin <feature-branch-name>
```

#### **5. Create a Pull Request (PR)**  
1. Go to the repository on GitHub.  
2. Click **Pull Requests** → **New Pull Request**.  
3. Select your branch and compare it with the `main` branch.  
4. Add a title and description, then submit the PR.  

#### **6. Review & Merge**  
- Wait for a review from a team member.  
- Once approved, merge the PR and delete the branch:  
  ```bash
  git checkout main
  git pull origin main

  **OPTIONAL**
  git branch -d <feature-branch-name>
  ```

#### **7. Sync Your Local Repo**  
To stay updated with the latest changes:  
```bash
git checkout main
git pull origin main
```

---
