import D3App from "d3js/app";
import React from "react";
import ReactDOM from "react-dom/client";
import "./index.css";

const root = ReactDOM.createRoot(
  document.getElementById("root") as HTMLElement
);

root.render(
  <React.StrictMode>
    <D3App data={[new Date().getTime()]} />
  </React.StrictMode>
);
