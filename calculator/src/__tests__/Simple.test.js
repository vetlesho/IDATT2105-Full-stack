// if we didnt define { globals: true } in vite.config.js
// import { test, expect } from "vitest";

import { test, expect } from "vitest";

const user = {
  name: "Vetle",
  age: 23,
};

test("Vetle er 22", () => {
  expect(user.name).toBe("Vetle");
  expect(user.age).toBe(23);
});

