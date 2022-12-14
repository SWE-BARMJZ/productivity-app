import { BACKEND_URL } from "./const";

export const registerUser = async (user) => {
  const url = `${BACKEND_URL}/api/user/register`;

  const response = await fetch(url, {
    method: "POST",
    headers: { "Content-type": "application/json" },
    body: JSON.stringify(user),
  }).catch((error) => {
    throw new Error("Problem connecting with the server!");
  });

  if (response.status !== 200) {
    const message = await response.text();
    throw new Error(message);
  }
  
  return response;
};

export const getUser = async (token) => {
  const url = `${BACKEND_URL}/api/user`;

  const response = await fetch(url, {
    method: "GET",
    headers: { Authorization: `Bearer ${token}` },
  });

  if (response.status !== 200) {
    throw new Error(response.status);
  }
  return response;
};
