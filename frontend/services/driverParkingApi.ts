import BASE_URL from "../constants/api";

export async function fetchNearbyParking(token: string) {
  const response = await fetch(`${BASE_URL}/api/driver/parking/nearby`, {
    method: "GET",
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
    },
  });

  if (!response.ok) {
    throw new Error("Failed to fetch parking spots");
  }

  return response.json();
}