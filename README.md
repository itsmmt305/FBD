# FBD – Freehand Boomer Downloader

![ic_launcher](https://github.com/user-attachments/assets/c79564c0-4de9-4ba8-ac61-790bd5fe6371)


FBD is an *Android WebView application* designed to **automate** a very specific workflow:  
load a target webpage, trigger a download action automatically, notify the user, and exit cleanly.

This app is built for **speed, automation, and minimal user interaction**, not for casual browsing.

---

## 🎯 Purpose

Most WebView apps are bloated wrappers.  
FBD is different: it exists to **do one job efficiently**.

**Workflow:**
1. Open a predefined webpage
2. Detect a specific page (`download.php`)
3. Automatically click the download trigger (`id="hdlink"`)
4. Wait for the system download to start
5. Show a confirmation toast
6. Close the app immediately

No extra taps. No confusion.

---

## 🚀 Features

- Lightweight **Android WebView**
- Fixed header + full WebView layout
- JavaScript injection for DOM interaction
- Automatic button click handling
- Download start detection
- Toast-based user feedback
- Auto-exit after task completion
- Minimal permissions
- No external browser dependency

---

## 🧱 Project Structure

app/  
└── src/  
└── main/  
├── java/com/yourpackage/fbd/  
│ &nbsp;&nbsp;&nbsp;&nbsp;└── MainActivity.kt  
│ &nbsp;&nbsp;&nbsp;&nbsp;└── LauncherActivity.kt  
├── res/  
│ &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;├── layout/  
│ &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;│ &nbsp;&nbsp;&nbsp;&nbsp;└── activity_main.xml  
│ &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;│ &nbsp;&nbsp;&nbsp;&nbsp;└── activity_launcher.xml  
│ &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└── values/  
│ &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;└── styles.xml  
└── AndroidManifest.xml  


---

## 🔐 Permissions

The application asks for only one permission:

```xml  

<uses-permission android:name="android.permission.INTERNET" />

```

---

## 🧠 Core Logic
### Page Detection

The app checks the loaded URL and triggers logic only when the target page is reached.

### JavaScript Injection

Used to automatically click the download button:

```javascript  

document.getElementById("hdlink")?.click();

```

### Download Handling

Once the system confirms the download has started:

- A toast is shown: “Download started”

- The application exits immediately

## ⚙️ Configuration

You can easily modify:

- Target URL

- Page match condition

- Button ID to trigger

- Auto-exit behavior

- Header UI elements

## ⚠️ Limitations

- DOM-dependent (site structure changes can break automation)

- Not intended for dynamic JS-heavy sites

- Requires the target site to allow WebView interaction

- This is a precision tool, not a universal downloader.

## 📦 Use Cases

- Automated file triggering

- Controlled download flows

- Single-purpose web automation

- Lightweight wrapper for repetitive tasks

- Can be used as a template for other such websites

## 📄 License

Open for educational and personal use.

## 👤 Author

**itsmmt305**

Built to finish the job and get out.



