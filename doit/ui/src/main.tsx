import React from "react";
import {createRoot} from 'react-dom/client';
import "./index.css";
import App from "app/app";

// mount to metis node
const metis = document.getElementById("metis") as HTMLElement;

const metisContainer = createRoot(metis);

metisContainer.render(
  <React.StrictMode>
    <App/>
  </React.StrictMode>
);
