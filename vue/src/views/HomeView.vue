<template>

  <el-container style="min-height: 100vh">
    <el-menu class="el-menu-vertical-demo" @open="handleOpen" @close="handleClose"
             :collapse="isCollapse"
             style="min-height: 100%; box-shadow: 2px 0 6px rgb(150 150 150)">
      <div style="height: 60px; line-height: 60px; text-align: center; background-color:#DDEBFF;">
        <img src="../assets/logo-nn.png" alt="" style="width: 50px; margin-top: 5px">
        <!--        <b style="color: black" v-show="false">教室管理系统</b>-->
      </div>

      <!--    左侧菜单-->
      <el-submenu index="1">
        <template slot="title">
          <i class="el-icon-school"></i>
          <span slot="title">系统管理</span>
        </template>
        <el-menu-item-group>
          <template slot="title">教室管理</template>
          <el-menu-item index="1-1">阶梯教室</el-menu-item>
          <el-menu-item index="1-2">普通教室</el-menu-item>
        </el-menu-item-group>
        <el-menu-item-group title="西区">
          <el-menu-item index="1-3">阶梯教室</el-menu-item>
          <el-menu-item index="1-4">普通教室</el-menu-item>
        </el-menu-item-group>
        <el-submenu index="1-5">
          <template slot="title">特殊教室</template>
          <el-menu-item index="1-5-1">听力教室</el-menu-item>
          <el-menu-item index="1-5-2">机房</el-menu-item>
        </el-submenu>
      </el-submenu>
      <el-submenu index="2">
        <template slot="title">
          <i class="el-icon-user"></i>
          <span slot="title">用户管理</span>
        </template>
        <el-menu-item-group>
          <template slot="title">分组一</template>
          <el-menu-item index="2-1">选项1</el-menu-item>
          <el-menu-item index="2-2">选项2</el-menu-item>
        </el-menu-item-group>
        <el-menu-item-group title="分组2">
          <el-menu-item index="2-3">选项3</el-menu-item>
        </el-menu-item-group>
        <el-submenu index="2-4">
          <template slot="title">选项4</template>
          <el-menu-item index="2-4-1">选项4-1</el-menu-item>
        </el-submenu>
      </el-submenu>
      <el-submenu index="3">
        <template slot="title">
          <i class="el-icon-more-outline"></i>
          <span slot="title">其它</span>
        </template>
        <el-menu-item-group>
          <template slot="title">分组一</template>
          <el-menu-item index="3-1">选项1</el-menu-item>
          <el-menu-item index="3-2">选项2</el-menu-item>
        </el-menu-item-group>
        <el-menu-item-group title="分组2">
          <el-menu-item index="3-3">选项3</el-menu-item>
        </el-menu-item-group>
        <el-submenu index="3-4">
          <template slot="title">选项4</template>
          <el-menu-item index="3-4-1">选项4-1</el-menu-item>
        </el-submenu>
      </el-submenu>
    </el-menu>
<!--        <el-aside width="sideWidth + 'px'" style="background-color: rgb(238, 241, 246); height:100%">
          <el-menu :default-openeds="['1', '3']"
                   background-color="rgb(255,255,255)"
                   text-color="rgb(0,0,0)"
                   active-text-color="rgb(197, 203, 211)"
                   style="height: 100%"
                   :collapse="isCollapse">

            <div style="height: 60px"; line-height>

            </div>
            <el-submenu index="1">
              <template slot="title">
                <i class="el-icon-message"></i>
                <span slot="title">教室管理</span>
              </template>
              <el-menu-item-group>
                <template slot="title">东区</template>
                <el-menu-item index="1-1">阶梯教室</el-menu-item>
                <el-menu-item index="1-2">普通教室</el-menu-item>
              </el-menu-item-group>
              <el-menu-item-group title="西区">
                <el-menu-item index="1-3">阶梯教室</el-menu-item>
                <el-menu-item index="1-4">普通教室</el-menu-item>
              </el-menu-item-group>
              <el-submenu index="1-5">
                <template slot="title">特殊教室</template>
                <el-menu-item index="1-5-1">听力教室</el-menu-item>
                <el-menu-item index="1-5-2">机房</el-menu-item>
              </el-submenu>
            </el-submenu>
            <el-submenu index="2">
              <template slot="title">
                <i class="el-icon-menu"></i>
                <span slot="title">用户管理</span>
              </template>
              <el-menu-item-group>
                <template slot="title">分组一</template>
                <el-menu-item index="2-1">选项1</el-menu-item>
                <el-menu-item index="2-2">选项2</el-menu-item>
              </el-menu-item-group>
              <el-menu-item-group title="分组2">
                <el-menu-item index="2-3">选项3</el-menu-item>
              </el-menu-item-group>
              <el-submenu index="2-4">
                <template slot="title">选项4</template>
                <el-menu-item index="2-4-1">选项4-1</el-menu-item>
              </el-submenu>
            </el-submenu>
            <el-submenu index="3">
              <template slot="title">
                <i class="el-icon-setting"></i>
                <span slot="title">其它</span>
              </template>
              <el-menu-item-group>
                <template slot="title">分组一</template>
                <el-menu-item index="3-1">选项1</el-menu-item>
                <el-menu-item index="3-2">选项2</el-menu-item>
              </el-menu-item-group>
              <el-menu-item-group title="分组2">
                <el-menu-item index="3-3">选项3</el-menu-item>
              </el-menu-item-group>
              <el-submenu index="3-4">
                <template slot="title">选项4</template>
                <el-menu-item index="3-4-1">选项4-1</el-menu-item>
              </el-submenu>
            </el-submenu>
          </el-menu>
        </el-aside>-->



    <el-container>
      <el-header
          style="text-align: left; font-size: 12px; border-bottom: 1px solid #ccc; display: flex; background-color: #DDEBFF">

        <div style="flex: 1; font-size: 18px">
          <!--          <span :class="collapseBtnClass" style="cursor: pointer" @click="collapse"></span>-->
          <el-radio-group v-model="isCollapse" style="margin: auto;">
            <el-radio-button :label="false">展开</el-radio-button>
            <el-radio-button :label="true">收起</el-radio-button>
          </el-radio-group>
        </div>

        <el-dropdown style="cursor: pointer">
          <i class="el-icon-setting" style="margin-right: 15px"></i>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item>个人信息</el-dropdown-item>
            <el-dropdown-item>退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
        <span>张浩男</span>
      </el-header>

      <el-main>
        <el-breadcrumb separator="/" style="margin-bottom: 10px">
          <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
          <el-breadcrumb-item><a href="/">教室管理</a></el-breadcrumb-item>
        </el-breadcrumb>

<!--        搜索栏-->
        <div>
          <el-input
              suffix-icon="el-icon-search"
              placeholder="请输入编号"
              v-model="c_name"
              style="width: 125px">

          </el-input>
          <el-input
              suffix-icon="el-icon-user-solid"
              placeholder="请输入容纳人数"
              v-model="c_volume"
              style="width: 150px; margin-left: 5px">

          </el-input>
          <el-input
              suffix-icon="el-icon-position"
              placeholder="请输入地址"
              v-model="c_address"
              style="width: 200px; margin-left: 5px">

          </el-input>
          <el-button
              type="primary"
              style="margin-left: 5px"
              icon="el-icon-search"
              @click="load">搜索
          </el-button>
          <el-button
              style="margin-left: 5px"
              icon="el-icon-refresh"
              @click="reset">重置
          </el-button>
        </div>

<!--        表格主体-->
        <div style="margin-top: 10px">
          <el-button type="success" icon="el-icon-circle-plus">新增</el-button>
          <el-button type="danger" icon="el-icon-delete-solid">批量删除</el-button>
          <el-button type="primary" icon="el-icon-download">导入</el-button>
          <el-button type="primary" icon="el-icon-upload2">导出</el-button>
        </div>
        <el-table :data="tableData" border stripe style="margin-top: 10px">
          <el-table-column prop="cname" label="教室编号" width="140">
          </el-table-column>
          <el-table-column prop="cvolume" label="容纳人数" width="120">
          </el-table-column>
          <el-table-column prop="caddress" label="地址">
          </el-table-column>
          <el-table-column label="操作">
            <template>
              <el-button type="primary" plain icon="el-icon-edit">编辑</el-button>
              <el-button type="danger" plain icon="el-icon-delete">删除</el-button>
            </template>
          </el-table-column>
        </el-table>

<!--        分页组件-->
        <div style="padding: 10px 0">
          <el-pagination
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
              :current-page="pageNum"
              :page-sizes="[5, 10, 20, 50]"
              :page-size="pageSize"
              layout="total, sizes, prev, pager, next, jumper"
              :total="total">
          </el-pagination>
        </div>
      </el-main>
    </el-container>
  </el-container>

</template>

<style>

.el-header {
  background-color: rgb(230, 255, 255);
  color: #333;
  line-height: 60px;
}

/*.el-aside {*/
/*  color: #333;*/
/*}*/

.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 200px;
  min-height: 400px;
}

</style>

<script>


import request from "@/utils/request";

export default {
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      c_name: "",
      c_volume: "",
      c_address: "",
      collapseBtnClass: 'el-icon-s-fold',
      isCollapse: false,
      logoTextShow: true,
      sideWidth: 200
    }
  },
  created() {
    //请求分页查询数据
    this.load()
  },
  methods: {
    handleOpen(key, keyPath) {
      console.log(key, keyPath);
    },
    handleClose(key, keyPath) {
      console.log(key, keyPath);
    },
    reset() {
      this.c_name = ""
      this.c_volume = ""
      this.c_address = ""
      this.load()
    },
    load() {
      request.get("http://localhost:8081/classroom/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          c_name: this.c_name,
          c_volume: this.c_volume,
          c_address: this.c_address
        }
      }).then(res => {
            console.log(res)
            this.tableData = res.records
            this.total = res.total
      })
      /*fetch("http://localhost:8081/classroom/page?pageNum=" + this.pageNum + "&pageSize=" + this.pageSize)
          .then(res => res.json()).then(res => {
        console.log(res)
        this.tableData = res.data
        this.total = res.total
      })*/
    },
    handleSizeChange(pageSize) {
      console.log(pageSize)
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      console.log(pageNum)
      this.pageNum = pageNum
      this.load()
    }
    // collapse() { //点击收缩按钮触发函数
    //   this.isCollapse = !this.isCollapse;
    //   if (this.isCollapse = false) {
    //     this.sideWidth = 64
    //     this.collapseBtnClass = 'el-icon-s-unfold'
    //     this.logoTextShow = false
    //   } else {
    //     this.sideWidth = 200
    //     this.collapseBtnClass = 'el-icon-s-fold'
    //     this.logoTextShow = true
    //   }
    // }
  }
}
</script>
