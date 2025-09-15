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
    "/auth/signup/kakao" : {
      "post" : {
        "tags" : [ "Auth" ],
        "summary" : "카카오 회원가입",
        "description" : "카카오 회원가입",
        "operationId" : "auth-signup-kakao",
        "requestBody" : {
          "content" : {
            "application/json" : {
              "schema" : {
                "$ref" : "#/components/schemas/auth-signup-kakao-290542722"
              },
              "examples" : {
                "auth-signup-kakao" : {
                  "value" : "{\n  \"name\" : \"홍길동\",\n  \"jobRoleId\" : \"6rOg7Zmp7IOd\",\n  \"careerYear\" : \"JUNIOR\",\n  \"requiredConsent\" : {\n    \"privacyPolicyAgreed\" : true,\n    \"serviceTermsAgreed\" : true\n  },\n  \"registrationToken\" : \"dummy-registration-token\"\n}"
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
    "/externals/invitations" : {
      "post" : {
        "tags" : [ "Invitation" ],
        "summary" : "초대장 요청 생성",
        "description" : "초대장 요청 생성",
        "operationId" : "create-invitation",
        "requestBody" : {
          "content" : {
            "application/json" : {
              "schema" : {
                "$ref" : "#/components/schemas/externals-invitations874860244"
              },
              "examples" : {
                "create-invitation" : {
                  "value" : "{\n  \"phone\" : \"010-1234-5678\"\n}"
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
                  "$ref" : "#/components/schemas/externals-invitations83146364"
                },
                "examples" : {
                  "create-invitation" : {
                    "value" : "{\n  \"data\" : {\n    \"message\" : \"초대장 요청이 전송되었습니다.\"\n  }\n}"
                  }
                }
              }
            }
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
                    "value" : "{\n  \"data\" : [ {\n    \"goal\" : {\n      \"id\" : \"goal-1\",\n      \"name\" : \"테스트 목표\",\n      \"duration\" : {\n        \"startDate\" : \"2025-09-15\",\n        \"endDate\" : \"2025-09-21\"\n      }\n    },\n    \"goalRetrospect\" : {\n      \"id\" : \"ZUurLAlviYyQP9K7isyno\",\n      \"isCompleted\" : true\n    }\n  } ]\n}"
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
                  "value" : "{\n  \"goalId\" : \"goalId\"\n}"
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
                    "value" : "{\n  \"data\" : {\n    \"id\" : \"id\"\n  }\n}"
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
                    "value" : "{\n  \"data\" : {\n    \"id\" : \"K_NO6XkZteFuqkzbSxboD\",\n    \"goalId\" : \"goalId\",\n    \"todoCompletedRate\" : 25,\n    \"analysis\" : {\n      \"summary\" : \"GROWIT MVP 개발과 서비스 기획을 병행하며 4주 목표를 달성\",\n      \"advice\" : \"모든 활동이 한 가지 핵심 가치에 연결되도록 중심축을 명확히 해보라냥!\"\n    },\n    \"content\" : \"이번 달 나는 '나만의 의미 있는 일'을 찾기 위해 다양한 프로젝트와 리서치에 몰입했다...\"\n  }\n}"
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
                "$ref" : "#/components/schemas/retrospects-id130578538"
              },
              "examples" : {
                "update-goal-retrospect" : {
                  "value" : "{\n  \"content\" : \"내 목표는 그로잇 완성\"\n}"
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
        "summary" : "내 목표 목록 조회",
        "description" : "내 목표 목록 조회",
        "operationId" : "get-my-goals",
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
                  "$ref" : "#/components/schemas/goals-1456783970"
                },
                "examples" : {
                  "get-my-goals" : {
                    "value" : "{\n  \"data\" : [ {\n    \"id\" : \"goal-1\",\n    \"name\" : \"테스트 목표\",\n    \"duration\" : {\n      \"startDate\" : \"2025-09-15\",\n      \"endDate\" : \"2025-09-21\"\n    },\n    \"toBe\" : \"TOBE\",\n    \"category\" : \"NETWORKING\",\n    \"plans\" : [ {\n      \"id\" : \"plan-1\",\n      \"weekOfMonth\" : 1,\n      \"content\" : \"그로잇 완성\",\n      \"duration\" : {\n        \"startDate\" : \"2025-09-15\",\n        \"endDate\" : \"2025-09-21\"\n      }\n    } ]\n  } ]\n}"
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
                "$ref" : "#/components/schemas/goals-id70548334"
              },
              "examples" : {
                "create-goal" : {
                  "value" : "{\n  \"name\" : \"내 목표는 그로잇 완성\",\n  \"duration\" : {\n    \"startDate\" : \"2025-09-22\",\n    \"endDate\" : \"2025-10-19\"\n  },\n  \"toBe\" : \"배포 완료\",\n  \"category\" : \"NETWORKING\",\n  \"plans\" : [ {\n    \"weekOfMonth\" : 1,\n    \"content\" : \"기획 및 설계 회의\"\n  }, {\n    \"weekOfMonth\" : 2,\n    \"content\" : \"디자인 시안 뽑기\"\n  }, {\n    \"weekOfMonth\" : 3,\n    \"content\" : \"프론트 개발 및 백 개발 완료\"\n  }, {\n    \"weekOfMonth\" : 4,\n    \"content\" : \"배포 완료\"\n  } ]\n}"
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
                    "value" : "{\n  \"data\" : {\n    \"id\" : \"XaFGWC3rsW_De_gn1PsJd\"\n  }\n}"
                  }
                }
              }
            }
          }
        }
      }
    },
    "/goals/{id}" : {
      "get" : {
        "tags" : [ "Goals" ],
        "summary" : "내 목표 조회",
        "description" : "내 목표 조회",
        "operationId" : "get-my-goal",
        "parameters" : [ {
          "name" : "id",
          "in" : "path",
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
                  "$ref" : "#/components/schemas/goals-id-1590510103"
                },
                "examples" : {
                  "get-my-goal" : {
                    "value" : "{\n  \"data\" : {\n    \"id\" : \"goal-1\",\n    \"name\" : \"테스트 목표\",\n    \"duration\" : {\n      \"startDate\" : \"2025-09-15\",\n      \"endDate\" : \"2025-09-21\"\n    },\n    \"toBe\" : \"TOBE\",\n    \"category\" : \"NETWORKING\",\n    \"plans\" : [ {\n      \"id\" : \"plan-1\",\n      \"weekOfMonth\" : 1,\n      \"content\" : \"그로잇 완성\",\n      \"duration\" : {\n        \"startDate\" : \"2025-09-15\",\n        \"endDate\" : \"2025-09-21\"\n      }\n    } ]\n  }\n}"
                  }
                }
              }
            }
          }
        }
      },
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
                "$ref" : "#/components/schemas/goals-id70548334"
              },
              "examples" : {
                "update-goal" : {
                  "value" : "{\n  \"name\" : \"내 목표는 그로잇 완성\",\n  \"duration\" : {\n    \"startDate\" : \"2025-09-22\",\n    \"endDate\" : \"2025-10-19\"\n  },\n  \"toBe\" : \"배포 완료\",\n  \"category\" : \"NETWORKING\",\n  \"plans\" : [ {\n    \"weekOfMonth\" : 1,\n    \"content\" : \"기획 및 설계 회의\"\n  }, {\n    \"weekOfMonth\" : 2,\n    \"content\" : \"디자인 시안 뽑기\"\n  }, {\n    \"weekOfMonth\" : 3,\n    \"content\" : \"프론트 개발 및 백 개발 완료\"\n  }, {\n    \"weekOfMonth\" : 4,\n    \"content\" : \"배포 완료\"\n  } ]\n}"
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
                    "value" : "{\n  \"data\" : \"목표가 수정 완료되었습니다.\"\n}"
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
                    "value" : "{\n  \"data\" : \"삭제 완료되었습니다.\"\n}"
                  }
                }
              }
            }
          }
        }
      }
    },
    "/goals/me/updatePlan" : {
      "put" : {
        "tags" : [ "Goals" ],
        "summary" : "계획 내용 수정",
        "description" : "계획 내용 수정",
        "operationId" : "update-plan-content",
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
        "requestBody" : {
          "content" : {
            "application/json" : {
              "schema" : {
                "$ref" : "#/components/schemas/goals-me-updatePlan460966616"
              },
              "examples" : {
                "update-plan-content" : {
                  "value" : "{\n  \"content\" : \"주 3회 운동으로 계획 수정\"\n}"
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
                  "update-plan-content" : {
                    "value" : "{\n  \"data\" : \"주간 목표 수정이 완료되었습니다.\"\n}"
                  }
                }
              }
            }
          }
        }
      }
    },
    "/mission" : {
      "get" : {
        "tags" : [ "Mission" ],
        "summary" : "오늘자 미션 조회",
        "description" : "현재 날짜의 요일에 해당하는 미션 목록을 조회합니다.",
        "operationId" : "get-mission",
        "responses" : {
          "200" : {
            "description" : "200",
            "content" : {
              "application/json" : {
                "schema" : {
                  "$ref" : "#/components/schemas/mission-1227183429"
                },
                "examples" : {
                  "get-mission" : {
                    "value" : "{\n  \"data\" : [ {\n    \"id\" : \"mission-1\",\n    \"dayOfWeek\" : \"MONDAY\",\n    \"content\" : \"오늘의 투두 작성하기\",\n    \"type\" : \"DAILY_TODO_WRITE\",\n    \"finished\" : false\n  }, {\n    \"id\" : \"mission-1\",\n    \"dayOfWeek\" : \"MONDAY\",\n    \"content\" : \"오늘의 투두 작성하기\",\n    \"type\" : \"DAILY_TODO_COMPLETE\",\n    \"finished\" : false\n  } ]\n}"
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
                    "value" : "{\n  \"data\" : {\n    \"message\" : \"성공은 매일 반복되는 작은 노력들의 합이다냥!\",\n    \"from\" : \"그로냥\"\n  }\n}"
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
        "summary" : "목표별 회고 목록 조회",
        "description" : "목표별 회고 목록 조회",
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
                  "get-retrospects-by-goal-id" : {
                    "value" : "{\n  \"data\" : [ {\n    \"plan\" : {\n      \"id\" : \"plan-1\",\n      \"weekOfMonth\" : 1,\n      \"isCurrentWeek\" : false,\n      \"content\" : \"주간 목표\"\n    },\n    \"retrospect\" : {\n      \"id\" : \"retrospect-123\",\n      \"content\" : \"이번 주에는 계획한 목표를 달성하기 위해 열심히 노력했습니다. 특히 새로운 기술을 배우는 것에 집중했습니다.\"\n    }\n  }, {\n    \"plan\" : {\n      \"id\" : \"plan-1\",\n      \"weekOfMonth\" : 1,\n      \"isCurrentWeek\" : false,\n      \"content\" : \"주간 목표\"\n    },\n    \"retrospect\" : {\n      \"id\" : \"retrospect-123\",\n      \"content\" : \"이번 주에는 계획한 목표를 달성하기 위해 열심히 노력했습니다. 특히 새로운 기술을 배우는 것에 집중했습니다.\"\n    }\n  } ]\n}"
                  },
                  "get-retrospect-by-filter" : {
                    "value" : "{\n  \"data\" : [ {\n    \"plan\" : {\n      \"id\" : \"plan-1\",\n      \"weekOfMonth\" : 1,\n      \"isCurrentWeek\" : false,\n      \"content\" : \"주간 목표\"\n    },\n    \"retrospect\" : {\n      \"id\" : \"retrospect-123\",\n      \"content\" : \"이번 주에는 계획한 목표를 달성하기 위해 열심히 노력했습니다. 특히 새로운 기술을 배우는 것에 집중했습니다.\"\n    }\n  } ]\n}"
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
                  "$ref" : "#/components/schemas/retrospects-id-950866177"
                },
                "examples" : {
                  "get-retrospect" : {
                    "value" : "{\n  \"data\" : {\n    \"plan\" : {\n      \"id\" : \"plan-1\",\n      \"weekOfMonth\" : 1,\n      \"isCurrentWeek\" : false,\n      \"content\" : \"주간 목표\"\n    },\n    \"retrospect\" : {\n      \"id\" : \"retrospect-123\",\n      \"content\" : \"이번 주에는 계획한 목표를 달성하기 위해 열심히 노력했습니다. 특히 새로운 기술을 배우는 것에 집중했습니다.\"\n    }\n  }\n}"
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
        "summary" : "오늘 미션 조회",
        "description" : "오늘 날짜의 미완료 ToDo 리스트를 조회합니다.",
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
                  "$ref" : "#/components/schemas/todos1427672143"
                },
                "examples" : {
                  "get-today-mission" : {
                    "value" : "{\n  \"data\" : [ {\n    \"id\" : \"id\",\n    \"goalId\" : \"goalId\",\n    \"planId\" : \"planId\",\n    \"content\" : \"테스트 할 일입니다.\",\n    \"date\" : \"2025-09-15\",\n    \"isCompleted\" : false\n  }, {\n    \"id\" : \"id2\",\n    \"goalId\" : \"goalId\",\n    \"planId\" : \"planId\",\n    \"content\" : \"테스트 할 일입니다.\",\n    \"date\" : \"2025-09-15\",\n    \"isCompleted\" : false\n  } ]\n}"
                  },
                  "get-weekly-plan" : {
                    "value" : "{\n  \"data\" : {\n    \"MONDAY\" : [ {\n      \"id\" : \"todoId\",\n      \"goalId\" : \"goal-123\",\n      \"planId\" : \"plan-456\",\n      \"date\" : \"2025-09-15\",\n      \"content\" : \"목표\",\n      \"isCompleted\" : true\n    } ],\n    \"TUESDAY\" : [ ],\n    \"WEDNESDAY\" : [ ],\n    \"THURSDAY\" : [ ],\n    \"FRIDAY\" : [ ],\n    \"SATURDAY\" : [ ],\n    \"SUNDAY\" : [ ]\n  }\n}"
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
                  "value" : "{\n  \"goalId\" : \"goal-1\",\n  \"date\" : \"2025-09-15\",\n  \"content\" : \"할 일 예시 내용입니다.\"\n}"
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
                    "value" : "{\n  \"data\" : {\n    \"id\" : \"todo-1\",\n    \"plan\" : {\n      \"id\" : \"plan-1\",\n      \"weekOfMonth\" : 1\n    }\n  }\n}"
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
                    "value" : "{\n  \"data\" : {\n    \"id\" : \"todo-1\",\n    \"goalId\" : \"goal-1\",\n    \"planId\" : \"plan-1\",\n    \"content\" : \"테스트 할 일입니다.\",\n    \"date\" : \"2025-09-15\",\n    \"isCompleted\" : false\n  }\n}"
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
                  "value" : "{\n  \"date\" : \"2025-09-15\",\n  \"content\" : \"수정된 내용\"\n}"
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
                    "value" : "{\n  \"data\" : {\n    \"id\" : \"todo-1\",\n    \"plan\" : {\n      \"id\" : \"plan-1\",\n      \"weekOfMonth\" : 1\n    }\n  }\n}"
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
                  "value" : "{\n  \"name\" : \"updatedName\",\n  \"jobRoleId\" : \"jobRoleId-1\",\n  \"careerYear\" : \"JUNIOR\"\n}"
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
                    "value" : "{\n  \"data\" : \"사용자 정보를 업데이트 하였습니다.\"\n}"
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
                    "value" : "{\n  \"data\" : \"탈퇴 처리 되었습니다.\"\n}"
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
                    "value" : "{\n  \"data\" : \"로그아웃 되었습니다.\"\n}"
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
                    "value" : "{\n  \"data\" : false\n}"
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
                    "value" : "{\n  \"data\" : \"온보딩 완료하였습니다.\"\n}"
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
      "externals-invitations83146364" : {
        "type" : "object",
        "properties" : {
          "data" : {
            "type" : "object",
            "properties" : {
              "message" : {
                "type" : "string",
                "description" : "응답 메시지"
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
      "auth-signup-kakao-290542722" : {
        "type" : "object",
        "properties" : {
          "registrationToken" : {
            "type" : "string",
            "description" : "카카오 등록 토큰"
          },
          "careerYear" : {
            "type" : "string",
            "description" : "경력 연차 (예: JUNIOR, MID, SENIOR)"
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
      "todos1427672143" : {
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
            }
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
      "goals-id70548334" : {
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
      "goals-id-1590510103" : {
        "type" : "object",
        "properties" : {
          "data" : {
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
      "goals-me-updatePlan460966616" : {
        "type" : "object",
        "properties" : {
          "content" : {
            "type" : "string",
            "description" : "수정할 계획 내용"
          }
        }
      },
      "mission-1227183429" : {
        "type" : "object",
        "properties" : {
          "data" : {
            "type" : "array",
            "description" : "미션 목록 배열",
            "items" : {
              "type" : "object",
              "properties" : {
                "dayOfWeek" : {
                  "type" : "string",
                  "description" : "요일"
                },
                "finished" : {
                  "type" : "boolean",
                  "description" : "완료 여부"
                },
                "id" : {
                  "type" : "string",
                  "description" : "미션 ID"
                },
                "type" : {
                  "type" : "string",
                  "description" : "미션 타입"
                },
                "content" : {
                  "type" : "string",
                  "description" : "미션 내용"
                }
              }
            }
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
      "externals-invitations874860244" : {
        "type" : "object",
        "properties" : {
          "phone" : {
            "type" : "string",
            "description" : "전화번호 (010-1234-5678 형식)"
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
      "goals-1456783970" : {
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