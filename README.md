# AutoSMS - SMS Auto Responder for Android

An Android application that automatically responds to incoming SMS text messages based on customizable trigger keywords. The app works whether the device is locked or unlocked.

## Features

- 🔄 **Automatic SMS Response**: Responds to incoming messages containing specified keywords
- 🔒 **Works When Locked**: Functions regardless of device lock state
- ⚙️ **Customizable Triggers**: Set custom trigger keywords and response messages
- 🔐 **Secure**: Requests only necessary permissions (SMS read, receive, and send)
- 💾 **Persistent Settings**: Settings persist across device reboots
- 📱 **Simple UI**: Easy-to-use interface for configuration

## How It Works

1. **Configure Settings**: Open the app and set:
   - Enable/disable auto-response with a toggle switch
   - A trigger keyword (e.g., "help", "info", "status")
   - A custom response message

2. **Automatic Responses**: When an SMS is received:
   - The app checks if auto-response is enabled
   - If the message contains the trigger keyword (case-insensitive)
   - An automatic response is sent to the sender

3. **Always Active**: The app uses BroadcastReceivers to listen for SMS, which works:
   - When the device is locked
   - When the device is unlocked
   - After device reboot (settings are preserved)

## Technical Details

### Architecture

- **SmsReceiver**: BroadcastReceiver that intercepts incoming SMS messages
- **BootReceiver**: Ensures the app functionality persists after device reboot
- **MainActivity**: User interface for configuring auto-response settings
- **SharedPreferences**: Stores settings persistently

### Permissions Required

- `RECEIVE_SMS`: To detect incoming SMS messages
- `READ_SMS`: To read the content of incoming messages
- `SEND_SMS`: To send automatic responses
- `RECEIVE_BOOT_COMPLETED`: To maintain functionality after reboot

### Build Requirements

- Android SDK 23 or higher (Android 6.0+)
- Target SDK: 34 (Android 14)
- Gradle 8.0+
- Java 8+

## Installation

### Building from Source

1. Clone the repository:
   ```bash
   git clone https://github.com/rtmoffat/autosms.git
   cd autosms
   ```

2. Build the project:
   ```bash
   ./gradlew assembleDebug
   ```

3. Install on device:
   ```bash
   ./gradlew installDebug
   ```

### Setting Up

1. Launch the app on your Android device
2. Grant the requested SMS permissions when prompted
3. Configure your auto-response settings:
   - Toggle "Enable Auto Response" ON
   - Enter a trigger keyword (e.g., "auto")
   - Enter your response message (e.g., "This is an automatic response")
4. Tap "Save Settings"

## Usage Example

**Configuration:**
- Trigger Keyword: `help`
- Response Message: `Thanks for your message! I'll get back to you soon.`

**Scenario:**
1. Someone sends you: "Hi, can you help me with this?"
2. The app detects the keyword "help" in the message
3. Automatically sends: "Thanks for your message! I'll get back to you soon."

## Security & Privacy

- The app only processes SMS messages locally on your device
- No data is transmitted to external servers
- Settings are stored locally using Android's SharedPreferences
- The app requests only the minimum necessary permissions

## Compatibility

- Minimum Android version: 6.0 (API 23)
- Target Android version: 14 (API 34)
- Tested on Android emulators and physical devices

## License

This project is open source. See the repository for license details.

## Contributing

Contributions are welcome! Please feel free to submit issues or pull requests.

## Troubleshooting

**Auto-response not working:**
- Ensure all SMS permissions are granted
- Check that auto-response is enabled in the app
- Verify the trigger keyword matches (case-insensitive)

**After device reboot:**
- Settings should persist automatically
- If not working, open the app once to re-initialize

## Development

### Project Structure
```
autosms/
├── app/
│   ├── src/main/
│   │   ├── java/com/rtmoffat/autosms/
│   │   │   ├── MainActivity.java       # Main UI Activity
│   │   │   ├── SmsReceiver.java        # SMS BroadcastReceiver
│   │   │   └── BootReceiver.java       # Boot BroadcastReceiver
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   │   └── activity_main.xml   # Main UI Layout
│   │   │   └── values/
│   │   │       ├── strings.xml         # String resources
│   │   │       └── themes.xml          # App theme
│   │   └── AndroidManifest.xml         # App manifest with permissions
│   └── build.gradle                     # App-level build config
├── build.gradle                         # Project-level build config
└── settings.gradle                      # Gradle settings
```
