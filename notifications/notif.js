var Firebase = require("firebase");
var request = require("request");

var myFirebaseRef = new Firebase("https://fiery-fire-8218.firebaseio.com/Timelines");

myFirebaseRef.on("child_added", function(snapshot, prevChildKey) {
   var key = snapshot.key();
   var eachTimeline = new Firebase("https;//fiery-fire-8218.firebaseio.com/Timelines/" + key + "/Events");

   eachTimeline.on("child_added", function(snapshot, prevChildKey) {
      // Create a json payload with the snapshot's attributes
      // Send the push notification to all users

      snapshot.ref().parent().parent().once("value", function(dataSnapshot) {

         var myJSONObject = {
            "group_id": "notifications",
            "recipients": {
               "custom_ids": []
            },
            "message": {
               "title": "Squadline",
               "body": "New Event in "
            }
         };

         dataSnapshot.child("Users").forEach(function(childSnapshot) {
            myJSONObject.recipients.custom_ids.push(childSnapshot.key());
         });

         myJSONObject.message.body = myJSONObject.message.body + dataSnapshot.child("Title").val() + "!";

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
            console.log("Notification Sent");
         });
      });
   });
});
