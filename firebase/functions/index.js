// index.js

const { onValueCreated } = require("firebase-functions/v2/database");
const { initializeApp } = require("firebase-admin/app");
const { getDatabase } = require("firebase-admin/database");
const { getMessaging } = require("firebase-admin/messaging");

initializeApp();

exports.sendMessageNotification = onValueCreated("/ChatRoom/{chatRoomId}/messages/{messageId}", async (event) => {
  const snapshot = event.data;
  const messageData = snapshot.val();

  const senderId = messageData.senderId;
  const chatRoomId = event.params.chatRoomId;

  const db = getDatabase();
  const chatRoomSnapshot = await db.ref(`/ChatRoom/${chatRoomId}`).once("value");
  const chatRoom = chatRoomSnapshot.val();
  const userIds = chatRoom.userIds;

  const receiverId = userIds.find((id) => id !== senderId);

  const presenceSnapshot = await db.ref(`/presence/${receiverId}`).once("value");
  const isOnline = presenceSnapshot.val();

  if (isOnline) {
    console.log(`${receiverId} is online. Not sending notification.`);
    return;
  }

  const userSnapshot = await db.ref(`/AllUsers/Users/${receiverId}`).once("value");
  const user = userSnapshot.val();
  const token = user.token;

  if (!token) {
    console.log("No token available");
    return;
  }

  const payload = {
    notification: {
      title: "New Message",
      body: messageData.message,
    },
    token: token,
  };

  try {
    await getMessaging().send(payload);
    console.log("Notification sent");
  } catch (error) {
    console.error("Error sending notification", error);
  }
});
