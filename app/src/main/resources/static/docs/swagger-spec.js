window.swaggerSpec={
  "openapi" : "3.0.1",
  "info" : {
    "title" : "GrowIT API Specification",
    "description" : "GrowIT description",
    "version" : "0.0.3"
  },
  "servers" : [ {
    "url" : "https://api.grow-it.me/"
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
                  "value" : "{\r\n  \"refreshToken\" : \"dummy-refresh-token\"\r\n}"
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
                  "$ref" : "#/components/schemas/auth-reissue-424105652"
                },
                "examples" : {
                  "auth-reissue" : {
                    "value" : "{\r\n  \"data\" : {\r\n    \"accessToken\" : \"accessToken\",\r\n    \"refreshToken\" : \"refreshToken\"\r\n  }\r\n}"
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
                  "value" : "{\r\n  \"email\" : \"test@example.com\",\r\n  \"password\" : \"securePass123\"\r\n}"
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
                  "$ref" : "#/components/schemas/auth-reissue-424105652"
                },
                "examples" : {
                  "auth-signin" : {
                    "value" : "{\r\n  \"data\" : {\r\n    \"accessToken\" : \"accessToken\",\r\n    \"refreshToken\" : \"refreshToken\"\r\n  }\r\n}"
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
                  "value" : "{\r\n  \"email\" : \"test@example.com\",\r\n  \"password\" : \"securePass123\",\r\n  \"name\" : \"홍길동\",\r\n  \"jobRoleId\" : \"6rOg7Zmp7IOd\",\r\n  \"careerYear\" : \"JUNIOR\",\r\n  \"requiredConsent\" : {\r\n    \"privacyPolicyAgreed\" : true,\r\n    \"serviceTermsAgreed\" : true\r\n  }\r\n}"
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
    "/goal-retrospects" : {
      "get" : {
        "tags" : [ "Goal Retrospects" ],
        "summary" : "연도별 목표+회고 목록 조회",
        "description" : "연도별 목표+회고 목록 조회",
        "operationId" : "get-goal-retrospects-by-year",
        "parameters" : [ {
          "name" : "year",
          "in" : "query",
          "description" : "조회 연도 (예: 2025)",
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
                  "$ref" : "#/components/schemas/goal-retrospects1943232045"
                },
                "examples" : {
                  "get-goal-retrospects-by-year" : {
                    "value" : "{\r\n  \"data\" : [ {\r\n    \"goal\" : {\r\n      \"id\" : \"goal-1\",\r\n      \"name\" : \"테스트 목표\",\r\n      \"duration\" : {\r\n        \"startDate\" : \"2025-08-11\",\r\n        \"endDate\" : \"2025-08-17\"\r\n      }\r\n    },\r\n    \"goalRetrospect\" : {\r\n      \"id\" : \"zDFs_5OktOpaLiwQn61AI\",\r\n      \"isCompleted\" : true\r\n    }\r\n  } ]\r\n}"
                  }
                }
              }
            }
          }
        }
      },
      "post" : {
        "tags" : [ "Goal Retrospects" ],
        "summary" : "목표 회고 생성",
        "description" : "목표 회고 생성",
        "operationId" : "create-goal-retrospect",
        "requestBody" : {
          "content" : {
            "application/json" : {
              "schema" : {
                "$ref" : "#/components/schemas/goal-retrospects-942583737"
              },
              "examples" : {
                "create-goal-retrospect" : {
                  "value" : "{\r\n  \"goalId\" : \"goalId\"\r\n}"
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
                  "$ref" : "#/components/schemas/goal-retrospects-1650437255"
                },
                "examples" : {
                  "create-goal-retrospect" : {
                    "value" : "{\r\n  \"data\" : {\r\n    \"id\" : \"id\"\r\n  }\r\n}"
                  }
                }
              }
            }
          }
        }
      }
    },
    "/goal-retrospects/{id}" : {
      "get" : {
        "tags" : [ "Goal Retrospects" ],
        "summary" : "목표회고 단건 조회",
        "description" : "목표회고 단건 조회",
        "operationId" : "get-goal-retrospect",
        "parameters" : [ {
          "name" : "id",
          "in" : "path",
          "description" : "목표회고 ID",
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
                  "$ref" : "#/components/schemas/goal-retrospects-id707367061"
                },
                "examples" : {
                  "get-goal-retrospect" : {
                    "value" : "{\r\n  \"data\" : {\r\n    \"id\" : \"JeB5Rgc_u4vFa11GPrSsa\",\r\n    \"goalId\" : \"goalId\",\r\n    \"todoCompletedRate\" : 25,\r\n    \"analysis\" : {\r\n      \"summary\" : \"GROWIT MVP 개발과 서비스 기획을 병행하며 4주 목표를 달성\",\r\n      \"advice\" : \"모든 활동이 한 가지 핵심 가치에 연결되도록 중심축을 명확히 해보라냥!\"\r\n    },\r\n    \"content\" : \"이번 달 나는 '나만의 의미 있는 일'을 찾기 위해 다양한 프로젝트와 리서치에 몰입했다...\"\r\n  }\r\n}"
                  }
                }
              }
            }
          }
        }
      },
      "patch" : {
        "tags" : [ "Goal Retrospects" ],
        "summary" : "목표 회고 수정",
        "description" : "목표 회고 수정",
        "operationId" : "update-goal-retrospect",
        "parameters" : [ {
          "name" : "id",
          "in" : "path",
          "description" : "목표 회고 ID",
          "required" : true,
          "schema" : {
            "type" : "string"
          }
        } ],
        "requestBody" : {
          "content" : {
            "application/json" : {
              "schema" : {
                "$ref" : "#/components/schemas/goal-retrospects-id130578538"
              },
              "examples" : {
                "update-goal-retrospect" : {
                  "value" : "{\r\n  \"content\" : \"내 목표는 그로잇 완성\"\r\n}"
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
    "/goals" : {
      "get" : {
        "tags" : [ "Goals" ],
        "summary" : "내 목표 조회",
        "description" : "내 목표 조회",
        "operationId" : "get-my-goal",
        "parameters" : [ {
          "name" : "status",
          "in" : "query",
          "description" : "목표 상태 필터 | Enum: NONE, ENDED, PROGRESS",
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
                  "$ref" : "#/components/schemas/goals-1516614006"
                },
                "examples" : {
                  "get-my-goal" : {
                    "value" : "{\r\n  \"data\" : [ {\r\n    \"id\" : \"goal-1\",\r\n    \"name\" : \"테스트 목표\",\r\n    \"duration\" : {\r\n      \"startDate\" : \"2025-08-11\",\r\n      \"endDate\" : \"2025-08-17\"\r\n    },\r\n    \"toBe\" : \"TOBE\",\r\n    \"category\" : \"NETWORKING\",\r\n    \"plans\" : [ {\r\n      \"id\" : \"plan-1\",\r\n      \"weekOfMonth\" : 1,\r\n      \"content\" : \"그로잇 완성\"\r\n    } ]\r\n  } ]\r\n}"
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
                "$ref" : "#/components/schemas/goals70548334"
              },
              "examples" : {
                "create-goal" : {
                  "value" : "{\r\n  \"name\" : \"내 목표는 그로잇 완성\",\r\n  \"duration\" : {\r\n    \"startDate\" : \"2025-08-18\",\r\n    \"endDate\" : \"2025-09-14\"\r\n  },\r\n  \"toBe\" : \"배포 완료\",\r\n  \"category\" : \"NETWORKING\",\r\n  \"plans\" : [ {\r\n    \"weekOfMonth\" : 1,\r\n    \"content\" : \"기획 및 설계 회의\"\r\n  }, {\r\n    \"weekOfMonth\" : 2,\r\n    \"content\" : \"디자인 시안 뽑기\"\r\n  }, {\r\n    \"weekOfMonth\" : 3,\r\n    \"content\" : \"프론트 개발 및 백 개발 완료\"\r\n  }, {\r\n    \"weekOfMonth\" : 4,\r\n    \"content\" : \"배포 완료\"\r\n  } ]\r\n}"
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
                    "value" : "{\r\n  \"data\" : {\r\n    \"id\" : \"l7P6QBBXaNKZRvairxtlR\"\r\n  }\r\n}"
                  }
                }
              }
            }
          }
        }
      }
    },
    "/goals/{id}" : {
      "put" : {
        "tags" : [ "Goals" ],
        "summary" : "목표 수정",
        "description" : "목표 수정",
        "operationId" : "update-goal",
        "parameters" : [ {
          "name" : "id",
          "in" : "path",
          "description" : "",
          "required" : true,
          "schema" : {
            "type" : "string"
          }
        } ],
        "requestBody" : {
          "content" : {
            "application/json" : {
              "schema" : {
                "$ref" : "#/components/schemas/goals70548334"
              },
              "examples" : {
                "update-goal" : {
                  "value" : "{\r\n  \"name\" : \"내 목표는 그로잇 완성\",\r\n  \"duration\" : {\r\n    \"startDate\" : \"2025-08-18\",\r\n    \"endDate\" : \"2025-09-14\"\r\n  },\r\n  \"toBe\" : \"배포 완료\",\r\n  \"category\" : \"NETWORKING\",\r\n  \"plans\" : [ {\r\n    \"weekOfMonth\" : 1,\r\n    \"content\" : \"기획 및 설계 회의\"\r\n  }, {\r\n    \"weekOfMonth\" : 2,\r\n    \"content\" : \"디자인 시안 뽑기\"\r\n  }, {\r\n    \"weekOfMonth\" : 3,\r\n    \"content\" : \"프론트 개발 및 백 개발 완료\"\r\n  }, {\r\n    \"weekOfMonth\" : 4,\r\n    \"content\" : \"배포 완료\"\r\n  } ]\r\n}"
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
                  "$ref" : "#/components/schemas/goals-id-1899666440"
                },
                "examples" : {
                  "update-goal" : {
                    "value" : "{\r\n  \"data\" : \"목표가 수정 완료되었습니다.\"\r\n}"
                  }
                }
              }
            }
          }
        }
      },
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
                    "value" : "{\r\n  \"data\" : \"삭제 완료되었습니다.\"\r\n}"
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
                    "value" : "{\r\n  \"data\" : {\r\n    \"jobRoles\" : [ {\r\n      \"id\" : \"dev\",\r\n      \"name\" : \"개발자\"\r\n    }, {\r\n      \"id\" : \"designer\",\r\n      \"name\" : \"디자이너\"\r\n    }, {\r\n      \"id\" : \"planner\",\r\n      \"name\" : \"기획자\"\r\n    } ]\r\n  }\r\n}"
                  }
                }
              }
            }
          }
        }
      }
    },
    "/resource/saying" : {
      "get" : {
        "tags" : [ "Saying" ],
        "summary" : "격언 조회",
        "description" : "격언 조회",
        "operationId" : "get-saying",
        "responses" : {
          "200" : {
            "description" : "200",
            "content" : {
              "application/json" : {
                "schema" : {
                  "$ref" : "#/components/schemas/resource-saying2030450560"
                },
                "examples" : {
                  "get-saying" : {
                    "value" : "{\r\n  \"data\" : {\r\n    \"message\" : \"성공은 매일 반복되는 작은 노력들의 합이다냥!\",\r\n    \"from\" : \"그로냥\"\r\n  }\r\n}"
                  }
                }
              }
            }
          }
        }
      }
    },
    "/retrospects" : {
      "get" : {
        "tags" : [ "Retrospects" ],
        "summary" : "회고 단건 조회 by goalId planId",
        "description" : "회고 단건 조회 by goalId planId",
        "operationId" : "get-retrospect",
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
                  "$ref" : "#/components/schemas/retrospects-1476343800"
                },
                "examples" : {
                  "get-retrospect-by-filter" : {
                    "value" : "{\r\n  \"data\" : [ {\r\n    \"plan\" : {\r\n      \"id\" : \"plan-1\",\r\n      \"weekOfMonth\" : 1,\r\n      \"isCurrentWeek\" : false,\r\n      \"content\" : \"주간 목표\"\r\n    },\r\n    \"retrospect\" : {\r\n      \"id\" : \"retrospect-123\",\r\n      \"content\" : \"이번 주에는 계획한 목표를 달성하기 위해 열심히 노력했습니다. 특히 새로운 기술을 배우는 것에 집중했습니다.\"\r\n    }\r\n  } ]\r\n}"
                  },
                  "get-retrospects-by-goal-id" : {
                    "value" : "{\r\n  \"data\" : [ {\r\n    \"plan\" : {\r\n      \"id\" : \"plan-1\",\r\n      \"weekOfMonth\" : 1,\r\n      \"isCurrentWeek\" : false,\r\n      \"content\" : \"주간 목표\"\r\n    },\r\n    \"retrospect\" : {\r\n      \"id\" : \"retrospect-123\",\r\n      \"content\" : \"이번 주에는 계획한 목표를 달성하기 위해 열심히 노력했습니다. 특히 새로운 기술을 배우는 것에 집중했습니다.\"\r\n    }\r\n  }, {\r\n    \"plan\" : {\r\n      \"id\" : \"plan-1\",\r\n      \"weekOfMonth\" : 1,\r\n      \"isCurrentWeek\" : false,\r\n      \"content\" : \"주간 목표\"\r\n    },\r\n    \"retrospect\" : {\r\n      \"id\" : \"retrospect-123\",\r\n      \"content\" : \"이번 주에는 계획한 목표를 달성하기 위해 열심히 노력했습니다. 특히 새로운 기술을 배우는 것에 집중했습니다.\"\r\n    }\r\n  } ]\r\n}"
                  }
                }
              }
            }
          }
        }
      },
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
                  "value" : "{\r\n  \"goalId\" : \"goal-123\",\r\n  \"planId\" : \"plan-456\",\r\n  \"content\" : \"이번 주에는 계획한 목표를 달성하기 위해 열심히 노력했습니다. 특히 새로운 기술을 배우는 것에 집중했습니다.\"\r\n}"
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
                    "value" : "{\r\n  \"data\" : {\r\n    \"id\" : \"retrospect-id\"\r\n  }\r\n}"
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
                    "value" : "{\r\n  \"data\" : {\r\n    \"isExist\" : true\r\n  }\r\n}"
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
                  "$ref" : "#/components/schemas/retrospects-id-950866177"
                },
                "examples" : {
                  "get-retrospect" : {
                    "value" : "{\r\n  \"data\" : {\r\n    \"plan\" : {\r\n      \"id\" : \"plan-1\",\r\n      \"weekOfMonth\" : 1,\r\n      \"isCurrentWeek\" : false,\r\n      \"content\" : \"주간 목표\"\r\n    },\r\n    \"retrospect\" : {\r\n      \"id\" : \"retrospect-123\",\r\n      \"content\" : \"이번 주에는 계획한 목표를 달성하기 위해 열심히 노력했습니다. 특히 새로운 기술을 배우는 것에 집중했습니다.\"\r\n    }\r\n  }\r\n}"
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
                "$ref" : "#/components/schemas/goal-retrospects-id130578538"
              },
              "examples" : {
                "update-retrospect" : {
                  "value" : "{\r\n  \"content\" : \"이번 주에는 계획한 목표를 달성하기 위해 열심히 노력했습니다. 특히 새로운 기술을 배우는 것에 집중했습니다.\"\r\n}"
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
        "summary" : "목표별 28일 기여도 리스트 조회",
        "description" : "특정 목표(goalId)에 대한 28일간의 기여도(상태) 리스트를 반환합니다.",
        "operationId" : "get-",
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
                  "$ref" : "#/components/schemas/todos-1546028826"
                },
                "examples" : {
                  "get-contribution" : {
                    "value" : "{\r\n  \"data\" : [ \"COMPLETED\", \"NOT_STARTED\", \"IN_PROGRESS\", \"NONE\" ]\r\n}"
                  },
                  "get-today-mission" : {
                    "value" : "{\r\n  \"data\" : [ {\r\n    \"id\" : \"id\",\r\n    \"goalId\" : \"goalId\",\r\n    \"planId\" : \"planId\",\r\n    \"content\" : \"테스트 할 일입니다.\",\r\n    \"date\" : \"2025-08-16\",\r\n    \"isCompleted\" : false\r\n  }, {\r\n    \"id\" : \"id2\",\r\n    \"goalId\" : \"goalId\",\r\n    \"planId\" : \"planId\",\r\n    \"content\" : \"테스트 할 일입니다.\",\r\n    \"date\" : \"2025-08-16\",\r\n    \"isCompleted\" : false\r\n  } ]\r\n}"
                  },
                  "get-weekly-plan" : {
                    "value" : "{\r\n  \"data\" : {\r\n    \"MONDAY\" : [ {\r\n      \"id\" : \"todoId\",\r\n      \"goalId\" : \"goal-123\",\r\n      \"planId\" : \"plan-456\",\r\n      \"date\" : \"2025-08-16\",\r\n      \"content\" : \"목표\",\r\n      \"isCompleted\" : true\r\n    } ],\r\n    \"TUESDAY\" : [ ],\r\n    \"WEDNESDAY\" : [ ],\r\n    \"THURSDAY\" : [ ],\r\n    \"FRIDAY\" : [ ],\r\n    \"SATURDAY\" : [ ],\r\n    \"SUNDAY\" : [ ]\r\n  }\r\n}"
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
                "$ref" : "#/components/schemas/todos881536773"
              },
              "examples" : {
                "create-todo" : {
                  "value" : "{\r\n  \"goalId\" : \"goal-1\",\r\n  \"date\" : \"2025-08-16\",\r\n  \"content\" : \"할 일 예시 내용입니다.\"\r\n}"
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
                  "$ref" : "#/components/schemas/todos-268181502"
                },
                "examples" : {
                  "create-todo" : {
                    "value" : "{\r\n  \"data\" : {\r\n    \"id\" : \"todo-1\",\r\n    \"plan\" : {\r\n      \"id\" : \"plan-1\",\r\n      \"weekOfMonth\" : 1\r\n    }\r\n  }\r\n}"
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
                    "value" : "{\r\n  \"data\" : {\r\n    \"id\" : \"todo-1\",\r\n    \"goalId\" : \"goal-1\",\r\n    \"planId\" : \"plan-1\",\r\n    \"content\" : \"테스트 할 일입니다.\",\r\n    \"date\" : \"2025-08-16\",\r\n    \"isCompleted\" : false\r\n  }\r\n}"
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
                  "value" : "{\r\n  \"date\" : \"2025-08-16\",\r\n  \"content\" : \"수정된 내용\"\r\n}"
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
                  "$ref" : "#/components/schemas/todos-id1880073077"
                },
                "examples" : {
                  "update-todo" : {
                    "value" : "{\r\n  \"data\" : {\r\n    \"id\" : \"todo-1\",\r\n    \"plan\" : {\r\n      \"id\" : \"plan-1\",\r\n      \"weekOfMonth\" : 1\r\n    }\r\n  }\r\n}"
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
                    "value" : "{\r\n  \"data\" : \"삭제가 완료되었습니다.\"\r\n}"
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
                  "value" : "{\r\n  \"isCompleted\" : true\r\n}"
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
                    "value" : "{\r\n  \"data\" : \"상태 변경이 완료되었습니다.\"\r\n}"
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
                    "value" : "{\r\n  \"data\" : \"HAPPY\"\r\n}"
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
                    "value" : "{\r\n  \"data\" : {\r\n    \"id\" : \"user-1\",\r\n    \"email\" : \"user@example.com\",\r\n    \"name\" : \"testUser\",\r\n    \"jobRole\" : {\r\n      \"id\" : \"dev\",\r\n      \"name\" : \"개발자\"\r\n    },\r\n    \"careerYear\" : \"JUNIOR\"\r\n  }\r\n}"
                  }
                }
              }
            }
          }
        }
      },
      "put" : {
        "tags" : [ "User" ],
        "summary" : "사용자 업데이트",
        "description" : "사용자 업데이트",
        "operationId" : "update-user",
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
        "requestBody" : {
          "content" : {
            "application/json" : {
              "schema" : {
                "$ref" : "#/components/schemas/users-myprofile-662092072"
              },
              "examples" : {
                "update-user" : {
                  "value" : "{\r\n  \"name\" : \"updatedName\",\r\n  \"jobRoleId\" : \"jobRoleId-1\",\r\n  \"careerYear\" : \"JUNIOR\"\r\n}"
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
                  "$ref" : "#/components/schemas/users-myprofile-995496151"
                },
                "examples" : {
                  "update-user" : {
                    "value" : "{\r\n  \"data\" : \"사용자 정보를 업데이트 하였습니다.\"\r\n}"
                  }
                }
              }
            }
          }
        }
      },
      "delete" : {
        "tags" : [ "User" ],
        "summary" : "사용자 탈퇴",
        "description" : "사용자 탈퇴",
        "operationId" : "delete-user",
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
                  "$ref" : "#/components/schemas/users-myprofile-1476599188"
                },
                "examples" : {
                  "delete-user" : {
                    "value" : "{\r\n  \"data\" : \"탈퇴 처리 되었습니다.\"\r\n}"
                  }
                }
              }
            }
          }
        }
      }
    },
    "/users/myprofile/logout" : {
      "post" : {
        "tags" : [ "User" ],
        "summary" : "사용자 로그아웃",
        "description" : "사용자 로그아웃",
        "operationId" : "logout-user",
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
                  "$ref" : "#/components/schemas/users-myprofile-logout1299858205"
                },
                "examples" : {
                  "logout-user" : {
                    "value" : "{\r\n  \"data\" : \"로그아웃 되었습니다.\"\r\n}"
                  }
                }
              }
            }
          }
        }
      }
    },
    "/users/myprofile/onboarding" : {
      "get" : {
        "tags" : [ "User" ],
        "summary" : "온보딩 여부 조회",
        "description" : "온보딩 여부 조회",
        "operationId" : "get-user-onboarding",
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
                  "$ref" : "#/components/schemas/users-myprofile-onboarding1739902847"
                },
                "examples" : {
                  "get-user-onboarding" : {
                    "value" : "{\r\n  \"data\" : false\r\n}"
                  }
                }
              }
            }
          }
        }
      },
      "put" : {
        "tags" : [ "User" ],
        "summary" : "온보딩 완료 처리",
        "description" : "온보딩 완료 처리",
        "operationId" : "complete-user-onboarding",
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
                  "$ref" : "#/components/schemas/users-myprofile-onboarding-878420979"
                },
                "examples" : {
                  "complete-user-onboarding" : {
                    "value" : "{\r\n  \"data\" : \"온보딩 완료하였습니다.\"\r\n}"
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
      "users-myprofile-995496151" : {
        "type" : "object",
        "properties" : {
          "data" : {
            "type" : "string",
            "description" : "업데이트 성공 메세지"
          }
        }
      },
      "todos-268181502" : {
        "type" : "object",
        "properties" : {
          "data" : {
            "type" : "object",
            "properties" : {
              "id" : {
                "type" : "string",
                "description" : "생성된 TODO ID"
              },
              "plan" : {
                "type" : "object",
                "properties" : {
                  "weekOfMonth" : {
                    "type" : "number",
                    "description" : "플랜의 월 기준 N번째 주"
                  },
                  "id" : {
                    "type" : "string",
                    "description" : "플랜 ID"
                  }
                }
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
      "resource-saying2030450560" : {
        "type" : "object",
        "properties" : {
          "data" : {
            "type" : "object",
            "properties" : {
              "from" : {
                "type" : "string",
                "description" : "격언 출처"
              },
              "message" : {
                "type" : "string",
                "description" : "격언 내용"
              }
            }
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
      "retrospects-1476343800" : {
        "type" : "object",
        "properties" : {
          "data" : {
            "type" : "array",
            "description" : "회고 목록",
            "items" : {
              "type" : "object",
              "properties" : {
                "plan" : {
                  "type" : "object",
                  "properties" : {
                    "weekOfMonth" : {
                      "type" : "string",
                      "description" : "계획 주차"
                    },
                    "id" : {
                      "type" : "string",
                      "description" : "계획 ID"
                    },
                    "content" : {
                      "type" : "string",
                      "description" : "계획 내용"
                    },
                    "isCurrentWeek" : {
                      "type" : "boolean",
                      "description" : "현재 주차 여부"
                    }
                  },
                  "description" : "계획"
                },
                "retrospect" : {
                  "type" : "object",
                  "properties" : {
                    "id" : {
                      "type" : "string",
                      "description" : "회고 ID"
                    },
                    "content" : {
                      "type" : "string",
                      "description" : "회고 내용"
                    }
                  },
                  "description" : "회고"
                }
              }
            }
          }
        }
      },
      "users-myprofile-onboarding1739902847" : {
        "type" : "object",
        "properties" : {
          "data" : {
            "type" : "boolean",
            "description" : "온보딩 진행 여부"
          }
        }
      },
      "todos881536773" : {
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
          "content" : {
            "type" : "string",
            "description" : "할 일 내용 (5자 이상 30자 미만)"
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
      "goals-1516614006" : {
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
                "toBe" : {
                  "type" : "string",
                  "description" : "목표 달성 후 상태"
                },
                "name" : {
                  "type" : "string",
                  "description" : "목표 이름"
                },
                "id" : {
                  "type" : "string",
                  "description" : "목표 ID"
                },
                "category" : {
                  "type" : "string",
                  "description" : "목표 카테고리 (예: PROFESSIONAL_GROWTH, CAREER_TRANSITION 등)"
                }
              }
            }
          }
        }
      },
      "users-myprofile-1476599188" : {
        "type" : "object",
        "properties" : {
          "data" : {
            "type" : "string",
            "description" : "탈퇴 성공 메세지"
          }
        }
      },
      "users-myprofile-662092072" : {
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
          "jobRoleId" : {
            "type" : "string",
            "description" : "직무 ID"
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
      "goal-retrospects-942583737" : {
        "type" : "object",
        "properties" : {
          "goalId" : {
            "type" : "string",
            "description" : "목표 아이디"
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
      "goal-retrospects-1650437255" : {
        "type" : "object",
        "properties" : {
          "data" : {
            "type" : "object",
            "properties" : {
              "id" : {
                "type" : "string",
                "description" : "목표 회고 아이디"
              }
            }
          }
        }
      },
      "goals70548334" : {
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
          "toBe" : {
            "type" : "string",
            "description" : "목표 달성 후 상태"
          },
          "name" : {
            "type" : "string",
            "description" : "목표 이름"
          },
          "category" : {
            "type" : "string",
            "description" : "목표 카테고리 (예: PROFESSIONAL_GROWTH, CAREER_TRANSITION 등)"
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
      "goal-retrospects-id130578538" : {
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
      "users-myprofile-logout1299858205" : {
        "type" : "object",
        "properties" : {
          "data" : {
            "type" : "string",
            "description" : "로그아웃 성공 메세지"
          }
        }
      },
      "users-myprofile-onboarding-878420979" : {
        "type" : "object",
        "properties" : {
          "data" : {
            "type" : "string",
            "description" : "온보딩 완료 성공 메세지"
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
      "retrospects-id-950866177" : {
        "type" : "object",
        "properties" : {
          "data" : {
            "type" : "object",
            "properties" : {
              "plan" : {
                "type" : "object",
                "properties" : {
                  "weekOfMonth" : {
                    "type" : "string",
                    "description" : "계획 주차"
                  },
                  "id" : {
                    "type" : "string",
                    "description" : "계획 ID"
                  },
                  "content" : {
                    "type" : "string",
                    "description" : "계획 내용"
                  },
                  "isCurrentWeek" : {
                    "type" : "boolean",
                    "description" : "현재 주차 여부"
                  }
                },
                "description" : "계획"
              },
              "retrospect" : {
                "type" : "object",
                "properties" : {
                  "id" : {
                    "type" : "string",
                    "description" : "회고 ID"
                  },
                  "content" : {
                    "type" : "string",
                    "description" : "회고 내용"
                  }
                },
                "description" : "회고"
              }
            },
            "description" : "회고 목록"
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
      "auth-reissue-424105652" : {
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
      "todos-id1880073077" : {
        "type" : "object",
        "properties" : {
          "data" : {
            "type" : "object",
            "properties" : {
              "id" : {
                "type" : "string",
                "description" : "수정된 TODO ID"
              },
              "plan" : {
                "type" : "object",
                "properties" : {
                  "weekOfMonth" : {
                    "type" : "number",
                    "description" : "플랜의 월 기준 N번째 주"
                  },
                  "id" : {
                    "type" : "string",
                    "description" : "플랜 ID"
                  }
                }
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
      },
      "goal-retrospects-id707367061" : {
        "type" : "object",
        "properties" : {
          "data" : {
            "type" : "object",
            "properties" : {
              "todoCompletedRate" : {
                "type" : "number",
                "description" : "할일 완료율"
              },
              "goalId" : {
                "type" : "string",
                "description" : "목표 ID"
              },
              "id" : {
                "type" : "string",
                "description" : "목표회고 ID"
              },
              "analysis" : {
                "type" : "object",
                "properties" : {
                  "summary" : {
                    "type" : "string",
                    "description" : "요약"
                  },
                  "advice" : {
                    "type" : "string",
                    "description" : "조언"
                  }
                },
                "description" : "분석 결과"
              },
              "content" : {
                "type" : "string",
                "description" : "회고 내용"
              }
            },
            "description" : "응답 데이터"
          }
        }
      },
      "goals-id-1899666440" : {
        "type" : "object",
        "properties" : {
          "data" : {
            "type" : "string",
            "description" : "성공 메세지"
          }
        }
      },
      "goal-retrospects1943232045" : {
        "type" : "object",
        "properties" : {
          "data" : {
            "type" : "array",
            "description" : "목록 데이터 배열",
            "items" : {
              "type" : "object",
              "properties" : {
                "goalRetrospect" : {
                  "type" : "object",
                  "properties" : {
                    "id" : {
                      "type" : "string",
                      "description" : "목표 회고 ID"
                    },
                    "isCompleted" : {
                      "type" : "boolean",
                      "description" : "회고 작성 완료 여부 (내용 미작성 포함)"
                    }
                  },
                  "description" : "목표 회고 정보 (null 가능)"
                },
                "goal" : {
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
                      "description" : "목표 기간"
                    },
                    "name" : {
                      "type" : "string",
                      "description" : "목표명"
                    },
                    "id" : {
                      "type" : "string",
                      "description" : "목표 ID"
                    }
                  },
                  "description" : "목표 정보"
                }
              }
            }
          }
        }
      },
      "todos-1546028826" : {
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
              "[]" : {
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
            },
            "description" : "28일간 각 날짜별 ToDoStatus(예: COMPLETED, NOT_STARTED, IN_PROGRESS, NONE)"
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