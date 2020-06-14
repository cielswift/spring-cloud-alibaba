package com.ciel.scaapi.util;

import java.util.LinkedList;
import java.util.Queue;

public class TreeNode {

    //定义该结点值
    int val;
    //定义左结点
    TreeNode left;
    //定义右结点
    TreeNode right;

    //定义一个构造函数
    TreeNode(int x) { val = x; }

    public static void main(String[] args) {
        TreeNode treeNode = new TreeNode(5);

        TreeNode treeNode3 = new TreeNode(3);
        treeNode3.left = new TreeNode(1);
        treeNode.left = treeNode3;
        treeNode.right = new TreeNode(7);

        treeNode.previous(treeNode);
    }
    /**
     * 先序遍历（递归）
     * @param node
     */
    public void previous(TreeNode node) {
        if (node == null) {
            return;
        }

        System.out.print(node.val+"\t");
        this.previous(node.left);
        this.previous(node.right);
    }
    /**
     * 中序遍历（递归）
     * @param node
     */
    public void middle(TreeNode node) {
        if (node == null) {
            return;
        }
        this.middle(node.left);
        System.out.print(node.val+"\t");
        this.middle(node.right);
    }
    /**
     * 后序遍历（递归）
     * @param node
     */
    public void next(TreeNode node) {
        if (node == null) {
            return;
        }
        this.next(node.left);
        this.next(node.right);
        System.out.print(node.val+"\t");
    }

    /**
     * 遍历二叉树
     * 层序遍历（非递归）
     * @param node
     */
    public void bfs(TreeNode node){
        if (node == null) {
            return;
        }
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(node);
        while (!queue.isEmpty()){
            TreeNode current = queue.poll();
            System.out.print(current.val + "\t");
            //如果当前节点的左节点不为空入队
            if(current.left != null)
            {
                queue.offer(current.left);
            }
            //如果当前节点的右节点不为空，把右节点入队
            if(current.right != null)
            {
                queue.offer(current.right);
            }
        }
    }

}
