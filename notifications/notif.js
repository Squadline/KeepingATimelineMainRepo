// Require the firebase and request modules
var Firebase = require("firebase");
var request = require("request");

// Get the Firebase timelines object
var myFirebaseRef = new Firebase("https://fiery-fire-8218.firebaseio.com/Timelines");

// Listen for new Timelines
myFirebaseRef.on("child_added", function(snapshot, prevChildKey) {
   // Save the key and get the reference to the Events table in the individual timeline
   var key = snapshot.key();
   var eachTimeline = new Firebase("https;//fiery-fire-8218.firebaseio.com/Timelines/" + key + "/Events");

   // Listen for new Events
   eachTimeline.on("child_added", function(snapshot, prevChildKey) {
      // Create a json payload with the snapshot's attributes
      // Send the push notification to all users

      // Once the listener is triggered, retrieve the data of the affected timeline
      snapshot.ref().parent().parent().once("value", function(dataSnapshot) {

         // Create a template JSON object
         var myJSONObject = {
            "group_id": "notifications",
            "recipients": {
               "custom_ids": []
            },
            "message": {
               "title": "Squadline",
               "body": "New Event in "
            },
            "custom_payload": "{\"key\":\"" + dataSnapshot.key() + "\", \"name\":\""
                  + dataSnapshot.child("Title").val() + "\"}"
         };

         // Set recipients as all users of the timeline
         dataSnapshot.child("Users").forEach(function(childSnapshot) {
            myJSONObject.recipients.custom_ids.push(childSnapshot.key());
         });

         // Record which timeline was graced with a new event
         myJSONObject.message.body = myJSONObject.message.body + dataSnapshot.child("Title").val() + "!";

         // Send the http request to the Batch notification server
         request({
            url: "https://api.batch.com/1.0/DEV574AD3E150F8C8BDDAAB694340E/transactional/send",
            method: "POST",
            json: true,
            body: myJSONObject,
            headers: {
               "Content-Type": "application/json",
               "X-Authorization": "6e20243b137c1d1de293115fd9dc2a11",
            }
         }, function(error, response, body) {
            // Log each notification sent
            console.log("Notification Sent");
         });
      });
   });
});
