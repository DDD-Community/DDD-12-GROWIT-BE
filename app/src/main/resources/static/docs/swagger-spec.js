window.swaggerSpec={
  "openapi" : "3.0.1",
  "info" : {
    "title" : "GrowIT API Specification",
    "description" : "GrowIT description",
    "version" : "0.0.2"
  },
  "servers" : [ {
    "url" : "http://growit-alb-alb-549641300.ap-northeast-2.elb.amazonaws.com/"
  } ],
  "tags" : [ ],
  "paths" : {
    "/auth/reissue" : {
      "post" : {
        "tags" : [ "Auth" ],
        "summary" : "토큰 재발급",
        "description" : "토큰 재발급",
        "operationId" : "auth-reissue",
        "requestBody" : {
          "content" : {
            "application/json" : {
              "schema" : {
                "$ref" : "#/components/schemas/auth-reissue356149288"
              },
              "examples" : {
                "auth-reissue" : {
                  "value" : "{\n  \"refreshToken\" : \"dummy-refresh-token\"\n}"
                }
              }
            }
          }
        },
        "responses" : {
          "200" : {
            "description" : "200",
            "content" : {
              "application/json" : {
                "schema" : {
                  "$ref" : "#/components/schemas/auth-signin-424105652"
                },
                "examples" : {
                  "auth-reissue" : {
                    "value" : "{\n  \"data\" : {\n    \"accessToken\" : \"accessToken\",\n    \"refreshToken\" : \"refreshToken\"\n  }\n}"
                  }
                }
              }
            }
          }
        }
      }
    },
    "/auth/signin" : {
      "post" : {
        "tags" : [ "Auth" ],
        "summary" : "사용자 로그인",
        "description" : "사용자 로그인",
        "operationId" : "auth-signin",
        "requestBody" : {
          "content" : {
            "application/json" : {
              "schema" : {
                "$ref" : "#/components/schemas/auth-signin32710318"
              },
              "examples" : {
                "auth-signin" : {
                  "value" : "{\n  \"email\" : \"test@example.com\",\n  \"password\" : \"securePass123\"\n}"
                }
              }
            }
          }
        },
        "responses" : {
          "200" : {
            "description" : "200",
            "content" : {
              "application/json" : {
                "schema" : {
                  "$ref" : "#/components/schemas/auth-signin-424105652"
                },
                "examples" : {
                  "auth-signin" : {
                    "value" : "{\n  \"data\" : {\n    \"accessToken\" : \"accessToken\",\n    \"refreshToken\" : \"refreshToken\"\n  }\n}"
                  }
                }
              }
            }
          }
        }
      }
    },
    "/auth/signup" : {
      "post" : {
        "tags" : [ "Auth" ],
        "summary" : "사용자 회원가입",
        "description" : "사용자 회원가입",
        "operationId" : "auth-signup",
        "requestBody" : {
          "content" : {
            "application/json" : {
              "schema" : {
                "$ref" : "#/components/schemas/auth-signup-1821282286"
              },
              "examples" : {
                "auth-signup" : {
                  "value" : "{\n  \"email\" : \"test@example.com\",\n  \"password\" : \"securePass123\",\n  \"name\" : \"홍길동\",\n  \"jobRoleId\" : \"6rOg7Zmp7IOd\",\n  \"careerYear\" : \"JUNIOR\",\n  \"requiredConsent\" : {\n    \"privacyPolicyAgreed\" : true,\n    \"serviceTermsAgreed\" : true\n  }\n}"
                }
              }
            }
          }
        },
        "responses" : {
          "201" : {
            "description" : "201"
          }
        }
      }
    },
    "/goals" : {
      "get" : {
        "tags" : [ "Goals" ],
        "summary" : "내 목표 조회",
        "description" : "내 목표 조회",
        "operationId" : "get-my-goal",
        "responses" : {
          "200" : {
            "description" : "200",
            "content" : {
              "application/json" : {
                "schema" : {
                  "$ref" : "#/components/schemas/goals595146955"
                },
                "examples" : {
                  "get-my-goal" : {
                    "value" : "{\n  \"data\" : [ {\n    \"id\" : \"goal-1\",\n    \"name\" : \"테스트 목표\",\n    \"duration\" : {\n      \"startDate\" : \"2025-07-07\",\n      \"endDate\" : \"2025-07-13\"\n    },\n    \"beforeAfter\" : {\n      \"asIs\" : \"ASIS\",\n      \"toBe\" : \"TOBE\"\n    },\n    \"plans\" : [ {\n      \"id\" : \"plan-1\",\n      \"weekOfMonth\" : 1,\n      \"content\" : \"그로잇 완성\"\n    } ]\n  } ]\n}"
                  }
                }
              }
            }
          }
        }
      },
      "post" : {
        "tags" : [ "Goals" ],
        "summary" : "목표 생성",
        "description" : "목표 생성",
        "operationId" : "create-goal",
        "requestBody" : {
          "content" : {
            "application/json" : {
              "schema" : {
                "$ref" : "#/components/schemas/goals-595510971"
              },
              "examples" : {
                "create-goal" : {
                  "value" : "{\n  \"name\" : \"내 목표는 그로잇 완성\",\n  \"duration\" : {\n    \"startDate\" : \"2025-07-14\",\n    \"endDate\" : \"2025-08-10\"\n  },\n  \"beforeAfter\" : {\n    \"asIs\" : \"기획 정의\",\n    \"toBe\" : \"배포 완료\"\n  },\n  \"plans\" : [ {\n    \"weekOfMonth\" : 1,\n    \"content\" : \"기획 및 설계 회의\"\n  }, {\n    \"weekOfMonth\" : 2,\n    \"content\" : \"디자인 시안 뽑기\"\n  }, {\n    \"weekOfMonth\" : 3,\n    \"content\" : \"프론트 개발 및 백 개발 완료\"\n  }, {\n    \"weekOfMonth\" : 4,\n    \"content\" : \"배포 완료\"\n  } ]\n}"
                }
              }
            }
          }
        },
        "responses" : {
          "201" : {
            "description" : "201",
            "content" : {
              "application/json" : {
                "schema" : {
                  "$ref" : "#/components/schemas/goals574842772"
                },
                "examples" : {
                  "create-goal" : {
                    "value" : "{\n  \"data\" : {\n    \"id\" : \"P1QQiAvsHUanCqzqCrSNT\"\n  }\n}"
                  }
                }
              }
            }
          }
        }
      }
    },
    "/goals/{id}" : {
      "delete" : {
        "tags" : [ "Goals" ],
        "summary" : "목표 삭제",
        "description" : "목표 삭제",
        "operationId" : "delete-goal",
        "parameters" : [ {
          "name" : "id",
          "in" : "path",
          "description" : "삭제할 목표 ID",
          "required" : true,
          "schema" : {
            "type" : "string"
          }
        } ],
        "responses" : {
          "200" : {
            "description" : "200",
            "content" : {
              "application/json" : {
                "schema" : {
                  "$ref" : "#/components/schemas/goals-id-1610094206"
                },
                "examples" : {
                  "delete-goal" : {
                    "value" : "{\n  \"data\" : \"삭제가 완료 되었습니다.\"\n}"
                  }
                }
              }
            }
          }
        }
      }
    },
    "/resource/jobroles" : {
      "get" : {
        "tags" : [ "JobRole" ],
        "summary" : "전체 직무 목록 조회",
        "description" : "전체 직무 목록 조회",
        "operationId" : "get-job-roles",
        "responses" : {
          "200" : {
            "description" : "200",
            "content" : {
              "application/json" : {
                "schema" : {
                  "$ref" : "#/components/schemas/resource-jobroles118231371"
                },
                "examples" : {
                  "get-job-roles" : {
                    "value" : "{\n  \"data\" : {\n    \"jobRoles\" : [ {\n      \"id\" : \"dev\",\n      \"name\" : \"개발자\"\n    }, {\n      \"id\" : \"designer\",\n      \"name\" : \"디자이너\"\n    }, {\n      \"id\" : \"planner\",\n      \"name\" : \"기획자\"\n    } ]\n  }\n}"
                  }
                }
              }
            }
          }
        }
      }
    },
    "/retrospects" : {
      "post" : {
        "tags" : [ "Retrospects" ],
        "summary" : "회고 생성",
        "description" : "회고 생성",
        "operationId" : "create-retrospect",
        "requestBody" : {
          "content" : {
            "application/json" : {
              "schema" : {
                "$ref" : "#/components/schemas/retrospects-597490674"
              },
              "examples" : {
                "create-retrospect" : {
                  "value" : "{\n  \"goalId\" : \"goal-123\",\n  \"planId\" : \"plan-456\",\n  \"content\" : \"이번 주에는 계획한 목표를 달성하기 위해 열심히 노력했습니다. 특히 새로운 기술을 배우는 것에 집중했습니다.\"\n}"
                }
              }
            }
          }
        },
        "responses" : {
          "201" : {
            "description" : "201",
            "content" : {
              "application/json" : {
                "schema" : {
                  "$ref" : "#/components/schemas/retrospects-1554052329"
                },
                "examples" : {
                  "create-retrospect" : {
                    "value" : "{\n  \"data\" : {\n    \"id\" : \"retrospect-id\"\n  }\n}"
                  }
                }
              }
            }
          }
        }
      }
    },
    "/retrospects/exists" : {
      "get" : {
        "tags" : [ "Retrospects" ],
        "summary" : "회고 존재 여부 확인",
        "description" : "회고 존재 여부 확인",
        "operationId" : "check-retrospect",
        "parameters" : [ {
          "name" : "goalId",
          "in" : "query",
          "description" : "목표 ID",
          "required" : true,
          "schema" : {
            "type" : "string"
          }
        }, {
          "name" : "planId",
          "in" : "query",
          "description" : "계획 ID",
          "required" : true,
          "schema" : {
            "type" : "string"
          }
        } ],
        "responses" : {
          "200" : {
            "description" : "200",
            "content" : {
              "application/json" : {
                "schema" : {
                  "$ref" : "#/components/schemas/retrospects-exists1042170112"
                },
                "examples" : {
                  "check-retrospect" : {
                    "value" : "{\n  \"data\" : {\n    \"isExist\" : true\n  }\n}"
                  }
                }
              }
            }
          }
        }
      }
    },
    "/retrospects/{id}" : {
      "get" : {
        "tags" : [ "Retrospects" ],
        "summary" : "회고 단건 조회",
        "description" : "회고 단건 조회",
        "operationId" : "get-retrospect",
        "parameters" : [ {
          "name" : "id",
          "in" : "path",
          "description" : "회고 ID",
          "required" : true,
          "schema" : {
            "type" : "string"
          }
        } ],
        "responses" : {
          "200" : {
            "description" : "200",
            "content" : {
              "application/json" : {
                "schema" : {
                  "$ref" : "#/components/schemas/retrospects-id1429283436"
                },
                "examples" : {
                  "get-retrospect" : {
                    "value" : "{\n  \"data\" : {\n    \"id\" : \"retrospect-123\",\n    \"goalId\" : \"goal-123\",\n    \"plan\" : {\n      \"id\" : \"plan-1\",\n      \"weekOfMonth\" : 1,\n      \"content\" : \"주간 목표\"\n    },\n    \"content\" : \"이번 주에는 계획한 목표를 달성하기 위해 열심히 노력했습니다. 특히 새로운 기술을 배우는 것에 집중했습니다.\"\n  }\n}"
                  }
                }
              }
            }
          }
        }
      },
      "put" : {
        "tags" : [ "Retrospects" ],
        "summary" : "회고 수정",
        "description" : "회고 수정",
        "operationId" : "update-retrospect",
        "parameters" : [ {
          "name" : "id",
          "in" : "path",
          "description" : "회고 ID",
          "required" : true,
          "schema" : {
            "type" : "string"
          }
        } ],
        "requestBody" : {
          "content" : {
            "application/json" : {
              "schema" : {
                "$ref" : "#/components/schemas/retrospects-id130578538"
              },
              "examples" : {
                "update-retrospect" : {
                  "value" : "{\n  \"content\" : \"이번 주에는 계획한 목표를 달성하기 위해 열심히 노력했습니다. 특히 새로운 기술을 배우는 것에 집중했습니다.\"\n}"
                }
              }
            }
          }
        },
        "responses" : {
          "200" : {
            "description" : "200"
          }
        }
      }
    },
    "/todos" : {
      "get" : {
        "tags" : [ "Todos" ],
        "summary" : "주간 할 일(Weekly Plan) 조회",
        "description" : "특정 목표/플랜에 대한 요일별 할 일을 조회합니다.",
        "operationId" : "get-weekly-plan",
        "parameters" : [ {
          "name" : "goalId",
          "in" : "query",
          "description" : "목표 ID",
          "required" : true,
          "schema" : {
            "type" : "string"
          }
        }, {
          "name" : "planId",
          "in" : "query",
          "description" : "계획 ID",
          "required" : true,
          "schema" : {
            "type" : "string"
          }
        } ],
        "responses" : {
          "200" : {
            "description" : "200",
            "content" : {
              "application/json" : {
                "schema" : {
                  "$ref" : "#/components/schemas/todos-448804850"
                },
                "examples" : {
                  "get-weekly-plan" : {
                    "value" : "{\n  \"data\" : {\n    \"MONDAY\" : [ {\n      \"id\" : \"todoId\",\n      \"goalId\" : \"goal-123\",\n      \"planId\" : \"plan-456\",\n      \"date\" : \"2025-07-11\",\n      \"content\" : \"목표\",\n      \"isCompleted\" : true\n    } ],\n    \"TUESDAY\" : [ ],\n    \"WEDNESDAY\" : [ ],\n    \"THURSDAY\" : [ ],\n    \"FRIDAY\" : [ ],\n    \"SATURDAY\" : [ ],\n    \"SUNDAY\" : [ ]\n  }\n}"
                  }
                }
              }
            }
          }
        }
      },
      "post" : {
        "tags" : [ "Todos" ],
        "summary" : "할 일(TODO) 생성",
        "description" : "할 일(TODO) 생성",
        "operationId" : "create-todo",
        "requestBody" : {
          "content" : {
            "application/json" : {
              "schema" : {
                "$ref" : "#/components/schemas/todos1279626246"
              },
              "examples" : {
                "create-todo" : {
                  "value" : "{\n  \"goalId\" : \"goal-1\",\n  \"planId\" : \"plan-1\",\n  \"date\" : \"2025-07-11\",\n  \"content\" : \"할 일 예시 내용입니다.\"\n}"
                }
              }
            }
          }
        },
        "responses" : {
          "201" : {
            "description" : "201",
            "content" : {
              "application/json" : {
                "schema" : {
                  "$ref" : "#/components/schemas/todos-1453646431"
                },
                "examples" : {
                  "create-todo" : {
                    "value" : "{\n  \"data\" : {\n    \"id\" : \"todo-1\"\n  }\n}"
                  }
                }
              }
            }
          }
        }
      }
    },
    "/todos/{id}" : {
      "get" : {
        "tags" : [ "Todos" ],
        "summary" : "할 일(TODO) 조회",
        "description" : "할 일(TODO) 조회",
        "operationId" : "get-todo",
        "parameters" : [ {
          "name" : "id",
          "in" : "path",
          "description" : "수정할 TODO ID",
          "required" : true,
          "schema" : {
            "type" : "string"
          }
        } ],
        "responses" : {
          "200" : {
            "description" : "200",
            "content" : {
              "application/json" : {
                "schema" : {
                  "$ref" : "#/components/schemas/todos-id203621875"
                },
                "examples" : {
                  "get-todo" : {
                    "value" : "{\n  \"data\" : {\n    \"id\" : \"todo-1\",\n    \"goalId\" : \"goal-1\",\n    \"planId\" : \"plan-1\",\n    \"content\" : \"테스트 할 일입니다.\",\n    \"date\" : \"2025-07-11\",\n    \"isCompleted\" : false\n  }\n}"
                  }
                }
              }
            }
          }
        }
      },
      "put" : {
        "tags" : [ "Todos" ],
        "summary" : "할 일(TODO) 수정",
        "description" : "할 일(TODO) 수정",
        "operationId" : "update-todo",
        "parameters" : [ {
          "name" : "id",
          "in" : "path",
          "description" : "수정할 TODO ID",
          "required" : true,
          "schema" : {
            "type" : "string"
          }
        } ],
        "requestBody" : {
          "content" : {
            "application/json" : {
              "schema" : {
                "$ref" : "#/components/schemas/todos-id-1719108715"
              },
              "examples" : {
                "update-todo" : {
                  "value" : "{\n  \"date\" : \"2025-07-11\",\n  \"content\" : \"수정된 내용\"\n}"
                }
              }
            }
          }
        },
        "responses" : {
          "200" : {
            "description" : "200",
            "content" : {
              "application/json" : {
                "schema" : {
                  "$ref" : "#/components/schemas/todos-id-1460495507"
                },
                "examples" : {
                  "update-todo" : {
                    "value" : "{\n  \"data\" : \"업데이트가 완료되었습니다.\"\n}"
                  }
                }
              }
            }
          }
        }
      },
      "delete" : {
        "tags" : [ "Todos" ],
        "summary" : "할 일(TODO) 삭제",
        "description" : "할 일을 삭제한다.",
        "operationId" : "delete-todo",
        "parameters" : [ {
          "name" : "id",
          "in" : "path",
          "description" : "상태를 변경할 TODO ID",
          "required" : true,
          "schema" : {
            "type" : "string"
          }
        } ],
        "responses" : {
          "200" : {
            "description" : "200",
            "content" : {
              "application/json" : {
                "schema" : {
                  "$ref" : "#/components/schemas/todos-id-53473476"
                },
                "examples" : {
                  "delete-todo" : {
                    "value" : "{\n  \"data\" : \"삭제가 완료되었습니다.\"\n}"
                  }
                }
              }
            }
          }
        }
      },
      "patch" : {
        "tags" : [ "Todos" ],
        "summary" : "할 일(TODO) 완료 상태 변경",
        "description" : "할 일의 완료 상태를 변경한다.",
        "operationId" : "status-change-todo",
        "parameters" : [ {
          "name" : "id",
          "in" : "path",
          "description" : "상태를 변경할 TODO ID",
          "required" : true,
          "schema" : {
            "type" : "string"
          }
        } ],
        "requestBody" : {
          "content" : {
            "application/json" : {
              "schema" : {
                "$ref" : "#/components/schemas/todos-id-1410595756"
              },
              "examples" : {
                "status-change-todo" : {
                  "value" : "{\n  \"isCompleted\" : true\n}"
                }
              }
            }
          }
        },
        "responses" : {
          "200" : {
            "description" : "200",
            "content" : {
              "application/json" : {
                "schema" : {
                  "$ref" : "#/components/schemas/todos-id176249715"
                },
                "examples" : {
                  "status-change-todo" : {
                    "value" : "{\n  \"data\" : \"상태 변경이 완료되었습니다.\"\n}"
                  }
                }
              }
            }
          }
        }
      }
    },
    "/todos/face/status" : {
      "get" : {
        "tags" : [ "Todos" ],
        "summary" : "그로냥 상태 조회",
        "description" : "사용자와 목표 ID로 얼굴 상태를 조회합니다.",
        "operationId" : "get-face-status",
        "parameters" : [ {
          "name" : "goalId",
          "in" : "query",
          "description" : "목표 ID",
          "required" : true,
          "schema" : {
            "type" : "string"
          }
        } ],
        "responses" : {
          "200" : {
            "description" : "200",
            "content" : {
              "application/json" : {
                "schema" : {
                  "$ref" : "#/components/schemas/todos-face-status253775494"
                },
                "examples" : {
                  "get-face-status" : {
                    "value" : "{\n  \"data\" : \"HAPPY\"\n}"
                  }
                }
              }
            }
          }
        }
      }
    },
    "/todos/home/today-mission" : {
      "get" : {
        "tags" : [ "Todos" ],
        "summary" : "오늘 미션 조회",
        "description" : "오늘 날짜의 미완료 ToDo 리스트를 조회합니다.",
        "operationId" : "get-today-mission",
        "responses" : {
          "200" : {
            "description" : "200",
            "content" : {
              "application/json" : {
                "schema" : {
                  "$ref" : "#/components/schemas/todos-home-today-mission-2063533231"
                },
                "examples" : {
                  "get-today-mission" : {
                    "value" : "{\n  \"data\" : [ {\n    \"id\" : \"id\",\n    \"goalId\" : \"goalId\",\n    \"planId\" : \"planId\",\n    \"content\" : \"테스트 할 일입니다.\",\n    \"date\" : \"2025-07-11\",\n    \"isCompleted\" : false\n  }, {\n    \"id\" : \"id2\",\n    \"goalId\" : \"goalId\",\n    \"planId\" : \"planId\",\n    \"content\" : \"테스트 할 일입니다.\",\n    \"date\" : \"2025-07-11\",\n    \"isCompleted\" : false\n  } ]\n}"
                  }
                }
              }
            }
          }
        }
      }
    },
    "/users/myprofile" : {
      "get" : {
        "tags" : [ "User" ],
        "summary" : "사용자 조회",
        "description" : "사용자 조회",
        "operationId" : "get-user",
        "parameters" : [ {
          "name" : "Authorization",
          "in" : "header",
          "description" : "JWT (Your Token)",
          "required" : true,
          "schema" : {
            "type" : "string"
          },
          "example" : "Bearer mock-jwt-token"
        } ],
        "responses" : {
          "200" : {
            "description" : "200",
            "content" : {
              "application/json" : {
                "schema" : {
                  "$ref" : "#/components/schemas/users-myprofile638373110"
                },
                "examples" : {
                  "get-user" : {
                    "value" : "{\n  \"data\" : {\n    \"id\" : \"user-1\",\n    \"email\" : \"user@example.com\",\n    \"name\" : \"testUser\",\n    \"jobRole\" : {\n      \"id\" : \"dev\",\n      \"name\" : \"개발자\"\n    },\n    \"careerYear\" : \"JUNIOR\"\n  }\n}"
                  }
                }
              }
            }
          }
        }
      }
    }
  },
  "components" : {
    "schemas" : {
      "todos1279626246" : {
        "type" : "object",
        "properties" : {
          "date" : {
            "type" : "string",
            "description" : "할 일 날짜 (yyyy-MM-dd)"
          },
          "goalId" : {
            "type" : "string",
            "description" : "목표 ID"
          },
          "planId" : {
            "type" : "string",
            "description" : "계획 ID"
          },
          "content" : {
            "type" : "string",
            "description" : "할 일 내용 (5자 이상 30자 미만)"
          }
        }
      },
      "goals-595510971" : {
        "type" : "object",
        "properties" : {
          "duration" : {
            "type" : "object",
            "properties" : {
              "endDate" : {
                "type" : "string",
                "description" : "종료일 (yyyy-MM-dd)"
              },
              "startDate" : {
                "type" : "string",
                "description" : "시작일 (yyyy-MM-dd)"
              }
            }
          },
          "plans" : {
            "type" : "array",
            "items" : {
              "type" : "object",
              "properties" : {
                "weekOfMonth" : {
                  "type" : "number",
                  "description" : "계획 주차"
                },
                "content" : {
                  "type" : "string",
                  "description" : "계획 내용"
                }
              }
            }
          },
          "name" : {
            "type" : "string",
            "description" : "목표 이름"
          },
          "beforeAfter" : {
            "type" : "object",
            "properties" : {
              "asIs" : {
                "type" : "string",
                "description" : "현재 상태(As-Is)"
              },
              "toBe" : {
                "type" : "string",
                "description" : "목표 달성 후 상태(To-Be)"
              }
            }
          }
        }
      },
      "goals-id-1610094206" : {
        "type" : "object",
        "properties" : {
          "data" : {
            "type" : "string",
            "description" : "삭제가 완료 되었습니다."
          }
        }
      },
      "resource-jobroles118231371" : {
        "type" : "object",
        "properties" : {
          "data" : {
            "type" : "object",
            "properties" : {
              "jobRoles" : {
                "type" : "array",
                "items" : {
                  "type" : "object",
                  "properties" : {
                    "name" : {
                      "type" : "string",
                      "description" : "직무 이름"
                    },
                    "id" : {
                      "type" : "string",
                      "description" : "직무 ID"
                    }
                  }
                }
              }
            }
          }
        }
      },
      "retrospects-1554052329" : {
        "type" : "object",
        "properties" : {
          "data" : {
            "type" : "object",
            "properties" : {
              "id" : {
                "type" : "string",
                "description" : "회고 ID"
              }
            }
          }
        }
      },
      "todos-id203621875" : {
        "type" : "object",
        "properties" : {
          "data" : {
            "type" : "object",
            "properties" : {
              "date" : {
                "type" : "string",
                "description" : "할 일 날짜 (yyyy-MM-dd)"
              },
              "goalId" : {
                "type" : "string",
                "description" : "목표 ID"
              },
              "planId" : {
                "type" : "string",
                "description" : "계획 ID"
              },
              "id" : {
                "type" : "string",
                "description" : "할일 ID"
              },
              "content" : {
                "type" : "string",
                "description" : "할 일 내용 (5자 이상 30자 미만)"
              },
              "isCompleted" : {
                "type" : "boolean",
                "description" : "완료 여부"
              }
            }
          }
        }
      },
      "todos-home-today-mission-2063533231" : {
        "type" : "object",
        "properties" : {
          "data" : {
            "type" : "array",
            "items" : {
              "type" : "object",
              "properties" : {
                "date" : {
                  "type" : "string",
                  "description" : "할 일 날짜"
                },
                "goalId" : {
                  "type" : "string",
                  "description" : "목표 ID"
                },
                "planId" : {
                  "type" : "string",
                  "description" : "계획 ID"
                },
                "id" : {
                  "type" : "string",
                  "description" : "TODO ID"
                },
                "content" : {
                  "type" : "string",
                  "description" : "내용"
                },
                "isCompleted" : {
                  "type" : "boolean",
                  "description" : "완료 여부"
                }
              }
            }
          }
        }
      },
      "auth-reissue356149288" : {
        "type" : "object",
        "properties" : {
          "refreshToken" : {
            "type" : "string",
            "description" : "리프레시 토큰"
          }
        }
      },
      "goals574842772" : {
        "type" : "object",
        "properties" : {
          "data" : {
            "type" : "object",
            "properties" : {
              "id" : {
                "type" : "string",
                "description" : "목표 ID"
              }
            }
          }
        }
      },
      "retrospects-id1429283436" : {
        "type" : "object",
        "properties" : {
          "data" : {
            "type" : "object",
            "properties" : {
              "goalId" : {
                "type" : "string",
                "description" : "목표 ID"
              },
              "id" : {
                "type" : "string",
                "description" : "회고 ID"
              },
              "plan" : {
                "type" : "object",
                "properties" : {
                  "weekOfMonth" : {
                    "type" : "number",
                    "description" : "계획 주차"
                  },
                  "id" : {
                    "type" : "string",
                    "description" : "계획 ID"
                  },
                  "content" : {
                    "type" : "string",
                    "description" : "계획 내용"
                  }
                }
              },
              "content" : {
                "type" : "string",
                "description" : "회고 내용"
              }
            }
          }
        }
      },
      "todos-id-1719108715" : {
        "type" : "object",
        "properties" : {
          "date" : {
            "type" : "string",
            "description" : "할 일 날짜 (yyyy-MM-dd)"
          },
          "content" : {
            "type" : "string",
            "description" : "수정할 할 일 내용 (5자 이상 30자 미만)"
          }
        }
      },
      "auth-signin32710318" : {
        "type" : "object",
        "properties" : {
          "password" : {
            "type" : "string",
            "description" : "사용자 비밀번호"
          },
          "email" : {
            "type" : "string",
            "description" : "사용자 이메일"
          }
        }
      },
      "retrospects-exists1042170112" : {
        "type" : "object",
        "properties" : {
          "data" : {
            "type" : "object",
            "properties" : {
              "isExist" : {
                "type" : "boolean",
                "description" : "회고 존재 여부 (true/false)"
              }
            }
          }
        }
      },
      "todos-id-1460495507" : {
        "type" : "object",
        "properties" : {
          "data" : {
            "type" : "string",
            "description" : "업데이트 결과 메시지"
          }
        }
      },
      "retrospects-id130578538" : {
        "type" : "object",
        "properties" : {
          "content" : {
            "type" : "string",
            "description" : "회고 내용"
          }
        }
      },
      "auth-signup-1821282286" : {
        "type" : "object",
        "properties" : {
          "careerYear" : {
            "type" : "string",
            "description" : "경력 연차 (예: JUNIOR, MID, SENIOR)"
          },
          "password" : {
            "type" : "string",
            "description" : "사용자 비밀번호"
          },
          "name" : {
            "type" : "string",
            "description" : "사용자 이름"
          },
          "requiredConsent" : {
            "type" : "object",
            "properties" : {
              "serviceTermsAgreed" : {
                "type" : "boolean",
                "description" : "서비스 약관 동의"
              },
              "privacyPolicyAgreed" : {
                "type" : "boolean",
                "description" : "개인정보 동의"
              }
            }
          },
          "jobRoleId" : {
            "type" : "string",
            "description" : "직무 ID"
          },
          "email" : {
            "type" : "string",
            "description" : "사용자 이메일"
          }
        }
      },
      "todos-face-status253775494" : {
        "type" : "object",
        "properties" : {
          "data" : {
            "type" : "string",
            "description" : "얼굴 상태 (예: SAD, NORMAL, HAPPY 등)"
          }
        }
      },
      "todos-id-53473476" : {
        "type" : "object",
        "properties" : {
          "data" : {
            "type" : "string",
            "description" : "결과 메시지"
          }
        }
      },
      "retrospects-597490674" : {
        "type" : "object",
        "properties" : {
          "goalId" : {
            "type" : "string",
            "description" : "목표 아이디"
          },
          "planId" : {
            "type" : "string",
            "description" : "계획 아이디"
          },
          "content" : {
            "type" : "string",
            "description" : "회고 내용"
          }
        }
      },
      "todos-448804850" : {
        "type" : "object",
        "properties" : {
          "data" : {
            "type" : "object",
            "properties" : {
              "WEDNESDAY" : {
                "type" : "array",
                "description" : "수요일 할 일 리스트(없을 수도 있음)",
                "items" : {
                  "oneOf" : [ {
                    "type" : "object"
                  }, {
                    "type" : "boolean"
                  }, {
                    "type" : "string"
                  }, {
                    "type" : "number"
                  } ]
                }
              },
              "MONDAY" : {
                "type" : "array",
                "items" : {
                  "type" : "object",
                  "properties" : {
                    "date" : {
                      "type" : "string",
                      "description" : "할 일 날짜"
                    },
                    "goalId" : {
                      "type" : "string",
                      "description" : "목표 ID"
                    },
                    "planId" : {
                      "type" : "string",
                      "description" : "계획 ID"
                    },
                    "id" : {
                      "type" : "string",
                      "description" : "TODO ID"
                    },
                    "content" : {
                      "type" : "string",
                      "description" : "내용"
                    },
                    "isCompleted" : {
                      "type" : "boolean",
                      "description" : "완료 여부"
                    }
                  }
                }
              },
              "THURSDAY" : {
                "type" : "array",
                "description" : "목요일 할 일 리스트(없을 수도 있음)",
                "items" : {
                  "oneOf" : [ {
                    "type" : "object"
                  }, {
                    "type" : "boolean"
                  }, {
                    "type" : "string"
                  }, {
                    "type" : "number"
                  } ]
                }
              },
              "SUNDAY" : {
                "type" : "array",
                "description" : "일요일 할 일 리스트(없을 수도 있음)",
                "items" : {
                  "oneOf" : [ {
                    "type" : "object"
                  }, {
                    "type" : "boolean"
                  }, {
                    "type" : "string"
                  }, {
                    "type" : "number"
                  } ]
                }
              },
              "FRIDAY" : {
                "type" : "array",
                "description" : "금요일 할 일 리스트(없을 수도 있음)",
                "items" : {
                  "oneOf" : [ {
                    "type" : "object"
                  }, {
                    "type" : "boolean"
                  }, {
                    "type" : "string"
                  }, {
                    "type" : "number"
                  } ]
                }
              },
              "TUESDAY" : {
                "type" : "array",
                "description" : "화요일 할 일 리스트(없을 수도 있음)",
                "items" : {
                  "oneOf" : [ {
                    "type" : "object"
                  }, {
                    "type" : "boolean"
                  }, {
                    "type" : "string"
                  }, {
                    "type" : "number"
                  } ]
                }
              },
              "SATURDAY" : {
                "type" : "array",
                "description" : "토요일 할 일 리스트(없을 수도 있음)",
                "items" : {
                  "oneOf" : [ {
                    "type" : "object"
                  }, {
                    "type" : "boolean"
                  }, {
                    "type" : "string"
                  }, {
                    "type" : "number"
                  } ]
                }
              }
            }
          }
        }
      },
      "goals595146955" : {
        "type" : "object",
        "properties" : {
          "data" : {
            "type" : "array",
            "items" : {
              "type" : "object",
              "properties" : {
                "duration" : {
                  "type" : "object",
                  "properties" : {
                    "endDate" : {
                      "type" : "string",
                      "description" : "종료일 (yyyy-MM-dd)"
                    },
                    "startDate" : {
                      "type" : "string",
                      "description" : "시작일 (yyyy-MM-dd)"
                    }
                  },
                  "description" : "기간 정보 객체"
                },
                "plans" : {
                  "type" : "array",
                  "description" : "계획 리스트",
                  "items" : {
                    "type" : "object",
                    "properties" : {
                      "weekOfMonth" : {
                        "type" : "number",
                        "description" : "주차"
                      },
                      "id" : {
                        "type" : "string",
                        "description" : "계획 ID"
                      },
                      "content" : {
                        "type" : "string",
                        "description" : "계획 내용"
                      }
                    }
                  }
                },
                "name" : {
                  "type" : "string",
                  "description" : "목표 이름"
                },
                "id" : {
                  "type" : "string",
                  "description" : "목표 ID"
                },
                "beforeAfter" : {
                  "type" : "object",
                  "properties" : {
                    "asIs" : {
                      "type" : "string",
                      "description" : "현재 상태(As-Is)"
                    },
                    "toBe" : {
                      "type" : "string",
                      "description" : "목표 달성 후 상태(To-Be)"
                    }
                  },
                  "description" : "전/후 상태 정보 객체"
                }
              }
            }
          }
        }
      },
      "todos-id176249715" : {
        "type" : "object",
        "properties" : {
          "data" : {
            "type" : "string",
            "description" : "변경 결과 메시지"
          }
        }
      },
      "auth-signin-424105652" : {
        "type" : "object",
        "properties" : {
          "data" : {
            "type" : "object",
            "properties" : {
              "accessToken" : {
                "type" : "string",
                "description" : "엑세스 토큰"
              },
              "refreshToken" : {
                "type" : "string",
                "description" : "리프레시 토큰"
              }
            }
          }
        }
      },
      "todos-id-1410595756" : {
        "type" : "object",
        "properties" : {
          "isCompleted" : {
            "type" : "boolean",
            "description" : "완료 여부"
          }
        }
      },
      "todos-1453646431" : {
        "type" : "object",
        "properties" : {
          "data" : {
            "type" : "object",
            "properties" : {
              "id" : {
                "type" : "string",
                "description" : "생성된 TODO ID"
              }
            }
          }
        }
      },
      "users-myprofile638373110" : {
        "type" : "object",
        "properties" : {
          "data" : {
            "type" : "object",
            "properties" : {
              "careerYear" : {
                "type" : "string",
                "description" : "경력 연차"
              },
              "name" : {
                "type" : "string",
                "description" : "이름"
              },
              "jobRole" : {
                "type" : "object",
                "properties" : {
                  "name" : {
                    "type" : "string",
                    "description" : "직무 이름"
                  },
                  "id" : {
                    "type" : "string",
                    "description" : "직무 ID"
                  }
                }
              },
              "id" : {
                "type" : "string",
                "description" : "사용자 ID"
              },
              "email" : {
                "type" : "string",
                "description" : "이메일"
              }
            }
          }
        }
      }
    },
    "securitySchemes" : {
      "bearerAuth" : {
        "type" : "http",
        "scheme" : "bearer",
        "bearerFormat" : "JWT",
        "name" : "Authorization",
        "in" : "header",
        "description" : "Use 'your-access-token' as the value of the Authorization header"
      }
    }
  },
  "security" : [ {
    "bearerAuth" : [ ]
  } ]
}