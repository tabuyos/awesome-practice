import * as d3 from "d3";
import { SimulationNodeDatum } from "d3-force";
import { useEffect, useRef } from "react";
import styles from "./app.module.css";
import graph from "./graph.json";

interface IProps {
  data?: number[];
}

interface INode {
  id: number;
  name: string;
  type: string;
  properties: object;
}

interface IAssignment {
  sourceID: number;
  targetID: number;
}

interface IAssociation {
  operations: string[];
  sourceID: number;
  targetID: number;
}

interface IGraph {
  nodes: INode[];
  assignments: IAssignment[];
  associations: IAssociation[];
}

const D3App = (props: IProps = { data: [1] }): JSX.Element => {
  const width = window.innerWidth;
  const height = window.innerHeight;

  const renderCounter = useRef(0);

  useEffect(() => {
    renderCounter.current++;
    if (renderCounter.current === 1) {
      const svg = d3
        .select("#svgCtx")
        .append("svg")
        .attr("width", width / 2)
        .attr("height", height)
        .style("background", "#FEF5E5");

      // const nodes: INode[] = graph.nodes;
      //
      // const cxfn: ValueFn<BaseType, INode, number> = (datum, index, groups) => {
      //   return 30 + index * 40;
      // }
      //
      // const cyfn: ValueFn<BaseType, INode, number> = (datum, index, groups) => {
      //   return 30 + index * 50;
      // }
      //
      // svg.selectAll("circle")
      //   .data(nodes)
      //   .join("circle")
      //   .attr("cx", cxfn)
      //   .attr("cy", cyfn)
      //   .attr("r", 20)
      //   .attr("fill", "green");

      // svg.append("rect")
      //   .attr("x", 30)
      //   .attr("y", 50)
      //   .attr("width", 50)
      //   .attr("height", 100)
      //   .attr("fill", "#00AEA6")
      //
      // svg.append("rect")
      //   .attr("x", width / 2 - 60)
      //   .attr("y", 50)
      //   .attr("width", 50)
      //   .attr("height", 100)
      //   .attr("fill", "#EB5C36")
      //
      // svg.append("circle")
      //   .attr("cx", 200)
      //   .attr("cy", 50)
      //   .attr("r", 20)
      //   .attr("fill", "green");
    }
  }, []);

  return <main className={styles.main} id={"svgCtx"}></main>;
};

export default D3App;
