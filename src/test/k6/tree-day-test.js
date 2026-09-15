import http from "k6/http";
import { check } from "k6";

export const options = {
  vus: 50,       // 동시 사용자 50명
  duration: "1m",
};

export default function () {
  const response = http.get(
    "http://127.0.0.1:7777/api/tree/day",
  );

  check(response, {
    "HTTP 200": (res) => res.status === 200,
  });
}