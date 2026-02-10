import { Stack } from "expo-router";

export default function DriverLayout() {
  return (
    <Stack screenOptions={{ headerShown: false }}>
      <Stack.Screen name="index" />
      <Stack.Screen name="register" />
      <Stack.Screen name="forgot-password" />
      <Stack.Screen name="dashboard" />
      <Stack.Screen name="bookings" />
      <Stack.Screen name="profile" />
    </Stack>
  );
}
