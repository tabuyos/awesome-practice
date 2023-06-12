import * as d3 from "d3";
import {useEffect, useRef} from "react";
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

const D3App = (props: IProps = {data: [1]}): JSX.Element => {

  const width = window.innerWidth;
  const height = window.innerHeight;

  const renderCounter = useRef(0);

  const intern = (value: any) => {
    return value !== null && typeof value === "object" ? value.valueOf() : value;
  }

  useEffect(() => {
    renderCounter.current++;
    if (renderCounter.current === 1) {
      const svg = d3
        .select("#svgCtx")
        .append("svg")
        .attr("width", width / 2)
        .attr("height", height)
        .style("background", "#FEF5E5");

      const nodes: any[] = graph.nodes;
      const links = graph.assignments.map(el => ({
        target: el.targetID,
        source: el.sourceID
      }));

      const colorScale = d3.scaleOrdinal(d3.quantize(d3.interpolateRainbow, nodes.length + 1))

      const forceLink = d3.forceLink(links)
        .distance(30)
        .strength(1)
        .id(({id}) => {
          return id;
        });
      const forceCenter = d3.forceCenter(width / 2, height / 2);

      d3.forceSimulation(nodes)
        .force("link", forceLink)
        .force('charge', d3.forceManyBody())
        .force("center", forceCenter)

      const chart = svg.append('g')
      const line = chart.append('g').selectAll().data(links).enter().append('g')
      const linkss = line.append('line').attr('stroke', '#ccc').attr('stroke-width', 1)
      const linksText = line
        .append('text')
        .text(function (d) {
          console.log(d)
          return "a"
        })
        .attr('fill', '#ccc')
      const nodesChart = chart
        .append('g')
        .selectAll()
        .data(nodes)
        .enter()
        .append('g')
        .attr('transform', function (d, i) {
          let cirX = d.x
          let cirY = d.y
          return 'translate(' + cirX + ',' + cirY + ')'
        })
      nodesChart
        .append('circle')
        .attr('r', function (d, i) {
          // 半径
          return 10
        })
        .attr('fill', function (d, i) {
          return colorScale(d.name)
        })

      nodesChart
        .append('text')
        .attr('x', -20)
        .attr('y', -5)
        .attr('dy', 10)
        .attr('font-size', 12)
        .text(function (d) {
          return d.name
        })
        .attr('fill', '#ccc')
        .attr('pointer-events', 'none')
        .style('user-select', 'none')

      function ticked() {
        linkss
          .attr('x1', function (d) {
            return d.source.x
          })
          .attr('y1', function (d) {
            return d.source.y
          })
          .attr('x2', function (d) {
            return d.target.x
          })
          .attr('y2', function (d) {
            return d.target.y
          })

        linksText
          .attr('x', function (d) {
            return (d.source.x + d.target.x) / 2
          })
          .attr('y', function (d) {
            return (d.source.y + d.target.y) / 2
          })

        nodesChart.attr('transform', function (d) {
          return 'translate(' + d.x + ',' + d.y + ')'
        })
      }

      // const force = d3
      //   .forceSimulation()
      //   .force('link', d3.forceLink())
      //   .force('charge', d3.forceManyBody())
      //   .force('center', d3.forceCenter(width / 2, height / 2));
      //
      // const forceNodes = force.nodes(nodes).on('tick', () => {
      //   console.log(this);
      // })

      // force.stop();
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
