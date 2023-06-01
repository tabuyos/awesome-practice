/*
 * copyright(c) 2018-2023 tabuyos all right reserved.
 */
package com.tabuyos.ngac.quickstart;

import gov.nist.csd.pm.exceptions.PMException;
import gov.nist.csd.pm.pdp.audit.Auditor;
import gov.nist.csd.pm.pdp.audit.PReviewAuditor;
import gov.nist.csd.pm.pdp.audit.model.Explain;
import gov.nist.csd.pm.pdp.audit.model.Path;
import gov.nist.csd.pm.pdp.decider.Decider;
import gov.nist.csd.pm.pdp.decider.PReviewDecider;
import gov.nist.csd.pm.pip.graph.Graph;
import gov.nist.csd.pm.pip.graph.GraphSerializer;
import gov.nist.csd.pm.pip.graph.MemGraph;
import gov.nist.csd.pm.pip.graph.model.nodes.Node;
import gov.nist.csd.pm.pip.graph.model.nodes.NodeType;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static gov.nist.csd.pm.pip.graph.model.nodes.NodeType.*;

/**
 * NgacMain
 *
 * @author tabuyos
 * @since 2023/5/31
 */
public class NgacMain {

  public static void main(String[] args) throws Exception {
    ex2();
  }

  public static void ex1() throws PMException {
    Graph graph = new MemGraph();
    // 创建名为: oNode, oaNode 的节点
    Node oNode = graph.createNode(1, "oNode", NodeType.O, null);
    Node oaNode = graph.createNode(2, "oaNode", NodeType.OA, null);
    Node uaNode = graph.createNode(3, "uaNode", NodeType.UA, null);
    // 为 oNode 分配一个 oaNode 的属性
    graph.assign(oNode.getID(), oaNode.getID());
    // 将 oaNode 和 uaNode 进行关联, 并为其指定权限
    graph.associate(uaNode.getID(), oaNode.getID(), Set.of("read", "write"));

    Decider decider = new PReviewDecider(graph);
    System.out.println(decider.list(oNode.getID(), 0, oaNode.getID()));
    System.out.println(decider.list(oNode.getID(), 0, uaNode.getID()));
    System.out.println(decider.list(oaNode.getID(), 0, uaNode.getID()));
    System.out.println(decider.list(uaNode.getID(), 0, oaNode.getID()));

    Auditor auditor = new PReviewAuditor(graph);
    Explain explain = auditor.explain(oNode.getID(), oaNode.getID());
    System.out.println(explain);
    System.out.println(explain.getPermissions());

    String json = GraphSerializer.toJson(graph);
    System.out.println(json);
  }

  public static void ex2() throws PMException {
    Graph graph = new MemGraph();

    Node u1 = graph.createNode(1001, "u1", U, null);
    Node u2 = graph.createNode(1002, "u2", U, null);

    Node o1 = graph.createNode(2001, "o1", O, null);

    Node rbac = graph.createNode(3001, "RBAC", PC, null);

    Node accounts = graph.createNode(4001, "Accounts", OA, null);

    Node teller = graph.createNode(5001, "Teller", UA, null);
    Node auditor = graph.createNode(5002, "Auditor", UA, null);

    graph.assign(accounts.getID(), rbac.getID());

    graph.assign(o1.getID(), accounts.getID());

    graph.assign(u1.getID(), teller.getID());
    graph.assign(u2.getID(), auditor.getID());

    graph.associate(teller.getID(), accounts.getID(), Set.of("r", "w"));
    graph.associate(auditor.getID(), accounts.getID(), Set.of("r", "x"));

    Node branches = graph.createNode(6001, "branches", PC, null);

    Node branchOa1 = graph.createNode(7001, "branch o1", OA, null);

    graph.assign(branchOa1.getID(), branches.getID());

    Node branchUa1 = graph.createNode(8001, "branch u1", UA, null);

    graph.assign(o1.getID(), branchOa1.getID());

    graph.assign(u1.getID(), branchUa1.getID());
    graph.assign(u2.getID(), branchUa1.getID());

    graph.associate(branchUa1.getID(), branchOa1.getID(), Set.of("r", "w", "x"));

    Decider decider = new PReviewDecider(graph, null);

    System.out.println(decider.list(u1.getID(), 0, o1.getID()));
    System.out.println(decider.list(u2.getID(), 0, o1.getID()));
    System.out.println(decider.list(u1.getID(), 0, branchOa1.getID()));
    System.out.println(decider.list(u2.getID(), 0, branchOa1.getID()));

    // String json = GraphSerializer.toJson(graph);
    // System.out.println(json);

    System.out.println("#########");

    Auditor reviewAuditor = new PReviewAuditor(graph);
    System.out.println(reviewAuditor.explain(u2.getID(), o1.getID()));
    System.out.println("---------");
    System.out.println(reviewAuditor.explain(u1.getID(), o1.getID()));
  }
}
