# UI Flow and User Guide

## App Interface

### Main Screen Layout

```
╔══════════════════════════════════════════════╗
║         SMS Auto Responder                   ║
║                                              ║
║  Automatically respond to SMS messages       ║
║  based on keywords                           ║
║                                              ║
║  ┌────────────────────────────────────────┐ ║
║  │                                        │ ║
║  │  Enable Auto Response    [    ON  ]   │ ║
║  │                                        │ ║
║  │  ┌──────────────────────────────────┐ │ ║
║  │  │ Trigger Keyword                  │ │ ║
║  │  │ (e.g., "help", "info")           │ │ ║
║  │  └──────────────────────────────────┘ │ ║
║  │                                        │ ║
║  │  ┌──────────────────────────────────┐ │ ║
║  │  │ Response Message                 │ │ ║
║  │  │                                  │ │ ║
║  │  │                                  │ │ ║
║  │  └──────────────────────────────────┘ │ ║
║  │                                        │ ║
║  │      ┌────────────────────┐            │ ║
║  │      │   Save Settings    │            │ ║
║  │      └────────────────────┘            │ ║
║  │                                        │ ║
║  └────────────────────────────────────────┘ ║
║                                              ║
║  When enabled, any SMS containing the        ║
║  trigger keyword will receive an automatic   ║
║  response. Works even when device is locked. ║
║                                              ║
╚══════════════════════════════════════════════╝
```

## User Journey

### First Time Setup

1. **Install & Launch**
   - User installs the app from APK
   - Launches the app from device

2. **Grant Permissions**
   - App requests SMS permissions
   - User must grant: RECEIVE_SMS, READ_SMS, SEND_SMS
   - Permission dialog appears automatically

3. **Configure Settings**
   - Toggle "Enable Auto Response" to ON
   - Enter trigger keyword (e.g., "auto", "help", "busy")
   - Enter response message (e.g., "Thanks for your message! I'll reply soon.")
   - Tap "Save Settings"

4. **Confirmation**
   - Toast message: "Settings saved successfully!"
   - App is now active and ready

### Daily Usage

#### Scenario 1: Device Unlocked
```
1. Someone sends: "Hi, can you help me?"
2. Device is unlocked, user is using it
3. SMS arrives → SmsReceiver activates
4. Keyword "help" detected in message
5. Auto-response sent immediately
6. User sees both messages in SMS app
```

#### Scenario 2: Device Locked
```
1. Someone sends: "Are you busy?"
2. Device is locked (screen off)
3. SMS arrives → SmsReceiver activates
4. Keyword "busy" detected in message
5. Auto-response sent immediately
6. No user interaction required
7. User can see sent message later in SMS app
```

#### Scenario 3: After Reboot
```
1. Device reboots (power cycle, update, etc.)
2. BootReceiver activates after boot
3. Settings loaded from SharedPreferences
4. Auto-response functionality restored
5. Works immediately on next SMS
```

## Configuration Examples

### Example 1: Work Hours Auto-Reply
```
Trigger Keyword: "work"
Response: "I'm currently at work and will respond during my break. Thanks!"
```

### Example 2: Driving Safety
```
Trigger Keyword: "urgent"
Response: "I'm driving right now. I'll call you back when safe to do so."
```

### Example 3: Vacation Auto-Reply
```
Trigger Keyword: "vacation"
Response: "I'm on vacation until next Monday. For urgent matters, contact support@company.com"
```

### Example 4: Information Request
```
Trigger Keyword: "hours"
Response: "Our business hours are Monday-Friday, 9 AM - 5 PM EST. Visit www.example.com for more info."
```

## Features Demonstration

### Case-Insensitive Matching
```
Trigger Keyword: "help"

Matches:
✓ "Can you help me?"
✓ "HELP!"
✓ "Need some HELP please"
✓ "helpful information"

Does NOT Match:
✗ "How are you?"
✗ "Let me know"
```

### Lock Screen Operation
```
State: Device Locked (Screen Off)
↓
SMS Received: "Need help"
↓
Background Process: SmsReceiver.onReceive()
↓
Check: Keyword match? YES
↓
Action: Send auto-response
↓
Result: Response sent
↓
State: Device still locked (no interruption)
```

### Settings Persistence
```
User Action                     Storage
─────────────────────────────────────────────
Save Settings              →    SharedPreferences
App Closed                 →    Settings retained
Device Reboot             →    Settings retained
Next SMS Received         →    Settings loaded & applied
```

## Technical Flow

### SMS Reception Flow
```
1. SMS Arrives at Device
   ↓
2. Android System Broadcasts Intent
   (android.provider.Telephony.SMS_RECEIVED)
   ↓
3. SmsReceiver.onReceive() Called
   ↓
4. Intent Verification
   ✓ Check intent action
   ✓ Extract PDUs
   ✓ Parse SMS message
   ↓
5. Settings Check
   ✓ Load SharedPreferences
   ✓ Check if enabled
   ✓ Get trigger keyword
   ✓ Get response message
   ↓
6. Keyword Matching
   ✓ Case-insensitive comparison
   ✓ Check if message contains keyword
   ↓
7. Send Response (if match)
   ✓ Get sender number
   ✓ Use SmsManager
   ✓ Send response message
   ↓
8. Log Results
   ✓ Log to Android system
```

## Security & Privacy

### Data Flow
```
SMS Received → Device Memory → Check Keyword → Send Response
                    ↓
            SharedPreferences (Local Storage)
```

**No External Communication:**
- ✓ All processing happens on device
- ✓ No internet connection required
- ✓ No data sent to external servers
- ✓ Complete privacy maintained

### Permission Usage
```
RECEIVE_SMS    →  Listen for incoming SMS
READ_SMS       →  Read message content
SEND_SMS       →  Send auto-response
BOOT_COMPLETED →  Restore functionality after reboot
```

## Troubleshooting

### Auto-Response Not Working?

1. **Check Permissions**
   - Open App Settings → Permissions
   - Ensure SMS permissions are granted

2. **Verify Settings**
   - Open app
   - Check toggle is ON
   - Verify keyword and message are set

3. **Test Keyword**
   - Send test message from another phone
   - Include exact trigger keyword
   - Check if response is sent

4. **After Reboot**
   - Settings should persist automatically
   - Try opening app once to re-initialize
   - Send test message

### Common Issues

**Issue:** No response sent
**Solution:** Check that the keyword is in the message (case doesn't matter)

**Issue:** Permission denied error
**Solution:** Grant SMS permissions in Android settings

**Issue:** Works when unlocked but not locked
**Solution:** This shouldn't happen - check battery optimization settings

**Issue:** Stopped working after update
**Solution:** Re-open app and verify settings are still saved

## Best Practices

### Choosing Keywords
- ✓ Use uncommon words to avoid false triggers
- ✓ Choose words relevant to your response
- ✓ Consider using unique codes (e.g., "AUTO123")
- ✗ Avoid common words like "the", "and", "a"

### Writing Responses
- ✓ Keep messages concise and clear
- ✓ Include expected response time
- ✓ Provide alternative contact methods if needed
- ✓ Be professional and courteous
- ✗ Don't include sensitive information

### Usage Tips
- Enable only when needed (e.g., driving, meetings)
- Update message based on context
- Disable when you're available to respond personally
- Test with a friend before relying on it
