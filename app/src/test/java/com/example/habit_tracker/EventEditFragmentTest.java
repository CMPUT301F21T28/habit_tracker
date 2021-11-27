package com.example.habit_tracker;

import static org.junit.jupiter.api.Assertions.assertEquals;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Base64;

public class EventEditFragmentTest {
    private EventEditFragment mockEventEdit;
    private String mockImageString ="/9j/4AAQSkZJRgABAQAAAQABAAD/4gIoSUNDX1BST0ZJTEUAAQEAAAIYAAAAAAIQAABtbnRyUkdCIFhZWiAAAAAAAAAAAAAAAABhY3NwAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAQAA9tYAAQAAAADTLQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAlkZXNjAAAA8AAAAHRyWFlaAAABZAAAABRnWFlaAAABeAAAABRiWFlaAAABjAAAABRyVFJDAAABoAAAAChnVFJDAAABoAAAAChiVFJDAAABoAAAACh3dHB0AAAByAAAABRjcHJ0AAAB3AAAADxtbHVjAAAAAAAAAAEAAAAMZW5VUwAAAFgAAAAcAHMAUgBHAEIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAFhZWiAAAAAAAABvogAAOPUAAAOQWFlaIAAAAAAAAGKZAAC3hQAAGNpYWVogAAAAAAAAJKAAAA+EAAC2z3BhcmEAAAAAAAQAAAACZmYAAPKnAAANWQAAE9AAAApbAAAAAAAAAABYWVogAAAAAAAA9tYAAQAAAADTLW1sdWMAAAAAAAAAAQAAAAxlblVTAAAAIAAAABwARwBvAG8AZwBsAGUAIABJAG4AYwAuACAAMgAwADEANv/bAEMAAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAf/bAEMBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAf/AABEIAKAAeAMBIgACEQEDEQH/xAAfAAABBAMBAQEBAAAAAAAAAAAGBQcICQADBAoCCwH/xAA/EAACAQMDAwIFAgQCCAYDAAABAgMEBREGEiEABzETQQgUIlFhMnEJFSOBQpEWFyRSobHB0TNDU2Jy8CWC8f/EABwBAAIDAQEBAQAAAAAAAAAAAAMFAgQGAQcACP/EADoRAAIBAgQCBwYFAwQDAAAAAAECEQMhAAQxQRJRBRMiYXGBoQYjMpHB8BRSsdHhQmLxFjRygpKio//aAAwDAQACEQMRAD8AlXcNPQEMUiGGyxaJtrH8HY0TsMnxtYE8jkkdANfYirFEkDM5z6c6Lk88BNggYH77i/nIJIOZA1FE/kIreSVjdZPtyD9BHP1YbPH05YDoYrbXC5Pqgx84b1YyAM4zll2xlfPO5vOPJx14KVB9PSNMesK51B5WM8gd/ptviPtXpt+XMRUgZKZ9VF/O8rEU+/0lieec4yGXC11MJYLzHxgvwpOeAolAcj7bWPP2OcyPr7TGVYU8qtjIGx1K7h7em3p8ge/1cDkHOCB3eidY2DYzyHLBeQeMbgExg44LN74zkdDKkTyG/wDGDpVOhI8O62h+npiPddRuA0csW4+42iRPvko5Urke4dvuB7dBtfSRjJUlWwcGMsFGM4URzKR7c+kAckjJHTzXKz7t8gV4xgsGhk4U5yCULBSfc/S2fBJ8dNve6Vot4LK/n6pUKs2ffd9IAOD4RyD9OehwZmbf48vkBiyvajvjvicNbcjOgJCjcc5GPTJX3AVw6H7kl08kHnkg9c6nO4eixPJxsU/3GYice4LfsccHdxEqsy7WUc4K5kjGPc5AfPgHCHH7dA9wWYbjIoPJIwOSM/ZSrj+6En9iCZjlGvLXbuPLBwANN9/27vM4FKqlY7n4Zck4ViSDk8kBShz5wdvOOMkkD9QrIcEHYeCzHb7eNy/RnkgYYHnGCelyr2ZJCyQeDuRmVcg8fpGCfblSPY5Bx0gSvNI2A5bJI37VDcAnl4yFfOMZKA4IGS3BIrAC5+fcBYC8j7jHcI9ZK65VPr2jI3rtcDxgOuCQDjGQwycHnkDlZGZFV3BZmaPAJ4y8qqvn3Xjnzkn356KJoArSsFADsPv4KxHHPjOATweSeOkapTMNOcfqlpsZGMA1EWB9zu3H25Ixj26kDNx3fTl4+WIFRrewH0vbuF/nFsJEsWC7e4MbDjOMHGSfwfAzk+5Ix0nSZxkefVjUk/YSx7hnz7nk8fnHPS3KNzbT4Z+DnGdpdvueRkc+PyCOeSanAUAfdH5xuGZUGfzn9yCPbroMEHkZwMiDGEmQEAtjwWOOecEN454zg+eMce3Wdba11jgyAASJDkef0nbkAfbjke3nrOiqSR4W1O0bffhc45j053CiVhuQlgOQB6b/AJwV+k5H/wAvtjjPQ7LblYEu7x8k8740x+VcSLx7lZF+3HHVqN4+C/t3VBns2pdbWN8NsiNdbLxRqT43x3O1yV0gXjCi4qeMEnppr38E+qYlkNh7h2S5Lzsp75p6rtTqvsj1tvr7srkngstBGAPCE9HqdGdI0pJoioBaUdD/AOpZW9D5YQ0+kMm8AZhRNgHSop21Ipsk/wDfxOK3bpbGKmQtDIgGUZokeTjxgxszjxlSGBByT5BLaXSJMupYqTngk5PHssqoV/cNk8ckHPU7tXfCf3vtUcrQacsWoI1DHdp3UdvywHIKxaiTT0hP+8vJzkLu4zWJ397pHsdqk6I19pbU+n75JboLosVVQUwimoaiSohhnhlhuUy1MLS0syCWnjkhLxOiyl0dUoOlVWCVab0iTYVFKgmBoTY90G15OGFHhq3pulSIPu3RyLiSQrEjlcb92Oe+yrGJF3KpzjkMpHGPqYqyZ55BOPsVwB00F4qVIfLA+cshVgQfwGLePIGMH7g8tldviO0ZcXIjrHp9xwRVU09PjJwV3ssYGD4PqEZ8HyOhGq7u6ZrARFXUsuQQXSeMtzngnO/zzzIQM+fGCCiSLL9Ttv8Az/NxQVFwQZnly8CP4mdMGte9NGWxNsbnIZWjHg4yGG088frwccY6B7hWoikPIjsxwCVB3ZDEAAAMBjOPq2n3H2GajVkdwP8AsBaVWJAkdmZBwG4BJV8jH4Xxk4ZevhDvUuxZmYnyPfawG3HHHJA4846r1ITvOhE/X5/xiygLD9+Vv38xfCVWSPUP4Kq0bn0wcqNrKMleFBwSPpAHJyT5PAfpJUDxuUED3KMCc+2cZ/vx+VWaM5B4GVmHPtmWL/pg88DznB60tTAhHJzuIJOAQeAB4JH+I48Y/J6gGmDPd52j0nf5479/PA/ODJ6v2DlQPyVjAA8n3OP2++ekOqGyNB/uSQefss0eM/kDkj3H36JW2kz4AI9VSDnPKCM8ffgecDyffwLVsgVH3ED+pFkEfeVN3k4z48eM+OOprqPEfr92/bHCYB/x67fcYTGciVEOTmQk4zj6oXbnA9ypJA+356+amXaVAIA3QqM58Fnc8cE8x/8A865palFniAPkknOB/wCXIBn9iR58ZPSXV16b1Ib/ABpx9ysc5yMe3jx9/GfJ/v71wA/f3bHzWupVwTkBJFwfOUSTzwPfGPb79Z0hVdaNrkHj+pjkgHKtx745PB4Hv46zo9NWg237uQxAsAYk+UfXH6Q7EgfbP5P45B4/445+460O2M5yc5yfHGPueul0+/B+49/+/XJIpx+3B5/z/b7ZGfP469AMRfTvx5wF4TIJ+4v42+4wg3MbomGPY4yPY/t/9GPHGevKP/G6tjW/vLpPVMEbjboay0tQyBhvC3zUwYuAcOI4l3lmz6caEkgDPXq5rxmJ/wBs/wCWPxz/APfJ68yP8bG2RVtXaJnlipmhtVqMtXOk8sMFGlVqeCX1I6SGpqyGluNPt9Cnlztw4UFT1nel6SFaLNYddTDECYVmCkgbkAn9IxoehKrCswF26qpAmZYKsTPMibd99Y88zajpqhSS6MGI+oYP0knkHwRzkEZ+4wekC7XOngUVKBWEDrUNnyERkMhXb7hAWBPjB/cBZoqy2Wentst303UTwbV9WG6vSLtWrhmzm60lulQeijKRJHGfK4weReWruT2t4J5rZUTi13eOf5S/WWvlBFrrVhk9Ojr55WzII2AEZfJywBBwvXLhDxK4ZQ/CJBUlQfjE7ECPEjnjSfiCYDIVJXiGjAGF7JI/qltN4J2EWGaSAegppFHDJGy//snn3zzjj7eQB05UdKI/RBIyzqQD5O5HJ+2RgZ/OM+/TZ9u5BWaas1QwKme3Uk3vwWhz/wAPGQOngKoDDxnDEcef0NwPzztP9/z0hzFqhH9x9IxdQwAdiBp5H6RrgeqoBlhjBXeBjAB5Uk+eMsM/nHn78U4EdPECSPoB+3iNjnP3ztx/bk+OluuTLSMOARIwHjBZgM/nP/DB48dClwlZYsZxgBB5AwI2ycjke/25Pg8dCWSR3X9RP7+OPvv5YQqmrji9XJH0s3jwCETJJ8+ODzk8/wBgK616DgMpG+PjPuHTz/cD7/bBHXJq7VNsskVRJcblQUCFmIetrIKZWIjAGwzPHuPBHGTzg5PUf9ad2rHpzTVHrSvjvkmlK65/ym36jpbJc2sdyukYrHa3W29z09ParjWqLdXGSmoqyeWM0dR6iIYm2ssrlMxmCBRo1apJ0pozyLflBg3tMb4pZnOZegPfVqVIDQO6qT4AmYvttfDr1d2VJdwIJw2MHxlW459sY8H3Pg9D9Rds7AGOS/JB5IEMx9/uRjAGPY+c9Beu6Tufo3Wdh0tqDRc9lt9dDT3PVmtY2fVumO2Wn5K2Wlrrxr6u00zWyyR2unhmuM9PLevqoxHIJkE8ZZtItaWqq7lansGlO4Vu7l6OtNksNTb9TWzTtTpykqrrWCvju0VLSVs1TWPTUtRCaRJpqqojnMBnhk2Py4boPpChl3zNah1NNAsmowDksQoAQEsCSZMhQBqZgFUnTWRr10y9GqatRyQOBW4BA4iSzcIItaJvpa+HsqLgSjkMeWx/nxkf9v8AnzjOm+kuzSKBu5LKSAMeWBP9x9+TnwfHWdU1pGLgE/PkO/720m71h2AjH6hci8kYx/35/wCP7f8AXpPmUjOQf3wMZHvn/n74/HWuS7RVCyfKsskJU7qgCX1ImyACIwpymcAlsEZGD7dDTyXGkk3pPJVxu4DR44wc8+Q2Bk8eOcYHWzZxEAcU2kaC8efiNNcYcITM2jY67cpjXe9tMdNeQY3/APiTnwfPj++f2z+3XnJ/jEWc3VpYgEJ/1fV1TG0jqkQmp+4GhLdEZJXIigTZfpczTOkS+GYEgH0aXDeIiZEMZdSwVgQSCPIz/lx158v4t1uqK9IaWn3erce1/cuKJUIDvLaLp2/1BGEx9W9ZLWjLzncBs+rpJ0vfL6xFSmZMx8S/IX+fjZv0KYzQ37LGOfZmB/19ceVHVli03Bo9KyO8mfXklyraeqsTXrTSWmG2AVHydRBKbhFUmq3RwtIJ52RvWaH5MbfWDXR2OKom0/NDaz89HT08FXNRzwVYaWoSamqvWNNPUI4eNwWYDGSCCOehfV2n+4KXaeOt0brV40rqtHE1gvUiiNaqVUV80rbQkYA2tgLwCAQOhWnsV7obrbbgmj9QU81JcKGcTGw3SIoEq4zKSWpQApiDq+eNuc4HR0ywSiwGZosSrMCIJhlgLIqDs7AEHWbze82b46yk5esAGQbgCGB4h7syeV76ADXFp/YSaS4dv9KSTg+qbHb1cEnysCpzkeOMr4/UM/bqRAoCqo7AnLA5A8FlY45z7k+x8+3UdvhqPr6SoqRlKtb6+4WtkYEEC33KsowpBAxs9ALzgggjHGOpjVFuBo0JTx4xzyEJX25AGPY5/wCPWOzae+qRbtt+t/3HyxpKR93TkyeBZ/8AETr46+fLEDNW92u4H8+vmnrJp2y2KWmqdQ1OmKrUDVVyOuNI9vy03dbUloegqKKjslRouFYqejtV/b1NRXGojgtzPTwVlRTNPWXK+a5qbDZ7t3M1Bpux90NIzd4u3+uFe2aaTT+gqAvFb9B6vs1EaG3VWs9aVkUlL81S6lq6e3xOsNthutQrSHr1XbK9NY6uo7lqiGqS8ay1VqvR10Vlkn7YpoHUM9TX9mVlM0UtLU/Ezemp6NbXTV1E96ioYlFk1W8cNFCgTQafvNNdrbrBJ9M6A7k32k7w9w2pQKOu7VfEzSpDDo/4e5nlo5YbbsSCmFxsl3sdJfCsiD5qzRfSPV+ieguiaeTydcZGi9aplsvVepVBrEu9JHYgVS6rLEmFAA0x5f0l0v0lUzWaonOVlpU8xWpolNuqARKjKoPVhS1hqxJJvhIt0FfNSUmvrNpIaT1vfhPJ8YnaGtp0EPZ74dbVK9Pca2g0/qyGPV+n7rrLt/Qf6SV1VQXC4a1vUt5lr9NwUvztugiIaDXml+yFxg72Q26q1d8I2v0TQ3w0dr6aoqa8aQ1WXp2vmrm0nrArRadaa9WbWxe9Q1NdqWqa+spp0pLlVelrmuGsbjXXPVl5gitXduVYKr+JBpqCngWit3YK2xpT2yy2qCvast5lvPaW37nTtleKvVrTzt8xWUNykWNP7S6m0T2+qY9eapsJ1X8LPdSMaV+Evt8bZQ6kTQ2qZjFT1WpTpfV1RBHpWR7mL1M96paq4X0pcZfSh2VJibQqiIAqKqKNFVQoHgAAMJCzMSWJYnUkkk+JN8Jtq0lV/DFebd8BF8udFrC1/E4g1JrDuXRrJpyt0jZtTJXaQraCz2yte9UtbVUcGjKi5Je7nVQ05a5ehJZ1FGaiqYW+dudEdme+HdDt926vVdqDSem7XoKG33i5XS2XesrKi52SpvN0M1ws9Hb7dMYLnXVlMUpqOIUwh+WlBmikZpEWmxan7bWq8fw/O4t7bVXdv4iZanU1P3apbjcdQ2PTdhutvpoIbbXm/JbtS3Oambt7eKg0cS0lCpvMEyTF3q+ojydqZOxfc/uP2tlvqalk0k2m7fLe4rc1qir5Ky31N6Zkt71twenEDXY0wDVkpl9H1/6fqelGp6dAbo6ov5npelRScNOhTHSFE8hUP/zaPWMOhFVgj9RO7b7/APu/6+BgeQf750gUrszDOcfSOfGdyn38kj/Pn346zrC9WBqDe/3HhHP0xu1ckWMeuw7vsfLH6Vdg7gV9w1ZUWy5Xa1/K08Uhp7hERAm8t/4e8YyCMgrgjyMY46PtSdxYNOQxPQ1NnurzskLIZ2eWPcwDSJ6RjClQScnH6TjHTLJ2AcNu/mlWD7kPgkj9j45Pvx+Pfd/qEzy90riAP/VP/fpuABEFoAE2aSdzOgnwt4wcICKTEEssaBbBed4jczcjlscSDgvdqrqRJqi/W0O8YJDVYYqCCQpLZOBnHJY/ck9UrfxNaSmrNQdv3oK6luEc2hviDo2elkEkfzNT28pTQwytwd3zkMLov1fUQRyObI5exscKE/zS4Hjx8w4HP4BGPb/74rf+NfQ50/de0VEsstSl31BqWy7qh2lIN8o7LadiuxIXeZ1AXyxx7rnpZ0uB+CqmZYGlAggn3tMHWfCR/GLvRahM2hVplagi0CaL3ECRH3yx4se5mr5W1Hf6Soro2FLeZII4DHTj0qdpmxHiNFJ2KwAZtzgADPHTXVs1tWpgqaWVklpJIKhJBhy0pkpZyJcKNyAOURcqFQe/PVl3dLWkbXGhtArZPmJ6u2W2VUqZEl3u8VKYt6OrqRu2ABgVVVUYwAIdXe8O1ddllvN0HyddcY4E/nVyVY1p6pRCCi1YVikblPrB3ADfubLEuTzJr0lIyqU1CKDFX4vhvApDXx3OuLeayxovDZl3MyPdzw+BatqLDTYaA3nL8LNatZQ3ZF8R6huVTtGcBLlUm6jAx4K1oxx759+LGXtcT0dNgfr2HAHjMLls/bGM+R9/2rH+FK5SS19+heoM8klRaa0SO5keRJ9P2qIs7kksxlpZd7HJLhieeRa5b9s1FTnGQRnHuR6Te/8An/brMZumBUcd4nx4Qbeo8PM4eU2lKZH5bH/iYFv5OnkKUNaR6Ph193Uhs7VrwN3dFJ3JjlEu8fEC14uEfwuSWXIBOn6d0mm1YsTPb2cKLpFOmYekCtktqi6ya8p3r9KyauGmu81DSGRJa7+IBKjC06otLUU1JWx6PoojThIbXVUui2K7qnTlVLljKjXNuoIrN3YaK0UTTXjuDriCuua0UJlsslTre9WKLX1VMsQf1u3FtuFTqqC4TT0rWulsssi3S006SV1NFd6ugs0dPeq+mj1JZbHdaX4Y7fa5xDU03c7uTcAW0x8bxgnNTS1NbXiZI6XUMIv99j2KLb3CrggUbP2B9rqXtVlOmqNLI1Ml/pjpzNezFRqlda34up0ZRy4bOIFp0+qSt1gIotxskGXbH5o9lfbmj7a9J+31Gj0dV6PPsf7e+0PsdVarmEzAz1boeugfPUwlKkaFOv1oK0G6xkgg1Gx0TpqOOpvDajqkqe4HbY09y/iL3KD5YUvcvtpNGarTtgs8MMNPbLuE0FS1lpnodPUOmmMjCKpqZah/muv5Bc9HWBl1Jrm1R3nsL3hgNq+CnRk1DFdl7eanqdlNDcWstUxj0bP/ADStpZv5nBVXKqjLtMp3KQ2S0FZapr5aLnc5brN8LElNVfG5qOeaomunxN6S1GHuWn7TLLK01fr+Wx2S23iy/wAu7jXG1UdNDVJSUtc1JLKIvkT6boCl97g0Iru3neum3/AJpFonqh2y1JVLGlnie10zfyzQ9R/NLvo5vVjrLnbYmgkaSYQU8m/e42WNcdn1XbrFc/hc19cP9I/jx1ozXjtz3hra2W8V+nNJRin1ALVTd4K1BrPT8ENh07rVI7Xa4DRrNeJqVMJdq1xD+g0prTROuu4ele4d1a+62sV1oLZqO7Pdqy+tW3CG3pKzm73BVrK0LFUxIJp1D7U2YCoo6l9JBrK2WCs07eqiS7/xQLZKtVom4xMl0v8ADoaaoglrFpb+0Ldq5Yv9Ws+rEajudWbmlPVyxwwi6NRoIm2OTX1Xq3uNV90zUP3G/wBJjT6yaujoI6oahprbbo7hHPHaUS2rLFNlHFGgpwV/p4Xkqemb5QgzBZfOHQ3+5w06H/3iEahXvy7LYM6XcXC4ByBgHOMjg+ce4OeP+nWdddGv9RQRxk+PbIcc+fckjPnjPWdY4xJjTG0XQa6DQxy+/HH6S8/fW2JnarHH3YAH8eftn88ePbpCqu/1KgOyLP7sB4/YEfb8dQ9lu+k6QGVda6LRwvMVVcdeX6RW44EVL2morYy5BwqXeRs/qkx5G7h3A7c0asbnf6aobgF7HoG7Va7sD9Ir+4Whty7gTkqDgAmNv09WH6W6LWfevU58L0F/LNmrqRvtaTMbrEyOZYj3JHilY+B7NNhHnvviVl8+I30InKRRgAE/VIB4+xxn+/n/AJmAPxLd0H163bq8uEgGle4lraSSOVgwhrpKeWQblI2hktrKSGU4JwR5Kjdu93byhDrT3XUk0I808Nn0tpyN+PcV161y6g54Detg8kMc9Qj7nd7qWo1hNa75Zb0+j4K6oqLRc5Y9K2r528UksdZY5aiFNDVVr1Ba6GlNwpDLV01VR3Ra6KthahgeopqxR0hn8lnMvUpZdKvGOrPE7IVAV0Y2pvU4pCmy6kyNpaZHJ5mhWptURQp4ogMpMrEdtVK3a/cfGKre52qb3BqHVOlZbtLQU2lZau3U9NBT21sCiklSnLtVUNQ//gwpu2kMzSMxcvtYQwu1Lb5aWGoNRDDVz3OWSqq6W32amnmWaRZPQlCW0wsFWdVMoiFRIIojLNIwkaSfnxWaO0lZ+6Xcm6HTt0rK2v1RfFqa4Veo1oqiKO4VUFNshts9Nb4s0aQFki/qMxaV2Bk2rXPqG526lcw27S9MFDllV6e91QD/AEgMRX19QhOETDSBvA4BB6Bk6lM004abSVTiJCBSwUBrlpPavJAnYAgQyrIxJZmUSWYAFi0FlZbAQCFme8xMHEkfhxukUWs54BIjGrsdimDoscZkeCu1Fa5GZYVRN7R2+FpCiKpZwQoHHVoR1pLY6WOmSyC4mJUw63JKXd/S28iWkZF5P/qMMY5HVP3ZKuWDWtirBT/y2aotENM8IpBTwetDcamd0KII1heU1LOgCsZA8jBW5bq1e6RsIEdhgPBGcjdtfdGuDnaTkAjH0/bJHS7PEpUsLMYIN/hAEWjlt6G2LVAB0AJNhMgwZMH66eGGP19U6Lvst0qLp2i009ZdI6qK63BqaGnrbiKqIw1SXC62+kpJ6tainZ4JvmKyRZopHRtyMQYC98TPpyntty7Z6dvek71HpxO3FXc4LlXaotp7SVU7xXXQdtW9XGuoLDa1ada2joIJqKGkFNKaKmRpi4sauNMdsj+cSsCVG7GUjwCSOPsDjIxjI6b25UdLUbmkggkblfUaJVmUj7SAK2cZ5HIyMHo+R6RfI1A6dYO1xOqVqiK5ChQXTihoAAHESQAANMU810Vla61V6rLq9Uy9T8PS6wvCjjZwA5bhAHEToACYAxWeNYWy30ugLbb7rnT+gzcKW36Y1BbKWezalttxmhkqbBr2noJLd/p9ao/R2wUOoam5pRieqipJI46mRW/i6+QVN8qr7R6f1S9LNLc+zNDW1lZZdO/D5f0NTVUFd2v05HHW0NDa6G4R2Kqi03WtUWuWLTttoZP9nMwabepu3ekb4++5WK2Vrksomrbdb7gxUMwG57jTVRGBgHaQCPPPPTRXn4fu3NaHL6O0ZMSD+rT1to3ycjPr00DsSeMf0x9+tHl/aN1ALZjMjSzqtblaWb1J5zhLW9nqbTw0MuZiCj1KfL+lQY8BO98R4l76zaXEHey5BNRfFdZg9vi7tXeagj0dc9OTLNZzbbj2/sMlvpYqmn0vO9rp6y00PzdRWBK2oZ53dma+33zU2ob9rrWOsIaGm1HrTWFw1LcltkUkNtequVFbnq5LdBUSz1MFB88tXFSRVUrTrFGvqFsh2kVV/DjoCiqBV02j7fRzU0gljqKGKkZYJFOVkRXUTKVIyriMFCM/ScY45+1lC24xS/j+qoVsfloyrn/IkZOeerdfptc3SFMuXFizFEBBlSAAjEWgkybzAAiTWodCtlKxqgKh4YVOsZgZsxJdFO9gAYIuWmA2lDUZ2HIOQPsBwDjPvycnnAA/z6zpw4+2ojbC3AxKCQPSDy8jPn19pwM+c4OMA4x1nS/rKbEkNHMFTY25eJJ7xGGaq4F1EiBqCLR4eHz5495Ft/h79vTDKuoO9Pd+8VEMs0E0FFVaPs9GT+uFkiptJS1SCSmkgmKtcJSGcpvyCOiWm+BX4Yoqb/8AKWPVt/rIXCyzXruRrjY0kZU4kpLberbQejMrK2BSqxikVhtP6d+o/iG0hou4TNrDuBozT0dVRMzvfdQ2nT0VPNR5aNWNyrRh6mGWTczOFIpYkT6iqtH/AFJ8f3YaxzSVK9yKK8woDT1a6Xt2odU08hGXgmW5WW01VsBUllYS1cSNFKSZy0UUZphcgsFMpS2HaQtBkfm473/icLQ3SlUx+JzJm46olBFpHu1URtpYwZgnD/03wz/DDYJGa29j9AVM0DlGfUlug1FOrKAdqVN7F2qBIwO9JdyGRGRzvU9UmfHyLXp7uFc30/HSWKay6mprfYKW2zR0AstHDp7TdyoTBbqURi101qhrqaitj0iiGSSjmWnK+n9EqtQ/xM+2lzrflND6N7jasukSsSI7dZrZb56YEg+tPPe6mvpzEzb4pZrVGqFijsokOKmPix+JW29ytSVd1uHZ/T9svdSyCpnr9dVF2uE6rTRUySXCistFaYoytNSxQ4WtkdNrDdtmmEgaiq/DTSmqkENwoESezqY4bQfCeUDDHJ08zTfrKzVivAV4qrO8EldOJm9ZO3haPp+8dje8um9NtfdGSXC/aotSVGob5cbI1psmoNaQ0NvN7lt1W00NnuNbeal7jcoFtqHfBQVrzCnEdOk4PqP4Zew1N8wlP200vCxB3y01opYqxSw4eQxxmYfqDByyg8cHcM0i2D4iO6mlLVHa9G3TTekLbT1BqKSO32Y3H0J/rImp11D/AD+FZFMjusjMrAsxTbuILwdsPjC73UGomW93nUPdumr/AEY6myw2tDPAd5US2me1W2SWkdd2HgFJNQzru30wlWOoiC+RrkFgEWBZFYyYju4ZO5n5a4trKECnVfh/uJA1EaGYAtAH74mrqD4ddB2x3ms1qtqRhtwikiiimQKclUO1IXK5HG2Nx4wWCnpmNT3ejt9+l0nKJY7hFaP5xTxwU1RU0xtEVSlEah5ViPyxhqGSGQVICjcGSWRMssuaa/Q6pstvvVXbb5YXuMQIoNSWuqsd3pJMkNBV0tSo2SbiCjRyT00wZWhll3YEe9caH1BS6qr9aadqLTXVNRYqCxm33Ognmlggt9Zca53pa2CrAQ1klegqI3t9SW+UgKMgD5XPSqFuFp7JMAzZiVEaEC36CCJxdo1hFyCTy3gzZhqZsJGuGCr5I5ImZHSRHlZ0aPEiODHGCwcFgQSOeeSPbGAD10a/Vjdz5yMrnk5BUgqSMYBxkffGOtHcrX1+s61EtfoWOG4IzE3C0VjJG7L9JeopzTCOpGAV3TwyyKMKnpYz0xdp+IXTtZMaDU1FPp6qJMa1jqZ7bI2cZklAMlMRgs7Sr6SEYEgyQCDK1QoeOJf7CGgiDtPMjBOtSYJg2+K06b6bgYdiZA0QBbBBI2ngE7myQTxv+/IP4PnoSr4iDwF8kEshz/yyfbkDH2yeluC7UVdSpNRVVPXUsoLxz0s0c8LKxYhgULABhzuyfOCM5wkVrLndHjb4IXOAT5O3x9wc4I/PBMYOkX/wLz5XOCYFbwQlBUYABK4JycEZXwByBnHJA5/y6buRVXJZVBzxkHznzj6gP7gc5A/LiXgb7dOA3naB+CZU4x7gkcHGD9+CSBSRRldxLtjzheOMj78/v7+McDphlRCEkavpvEC0fPFHNfGv/H6nCHKSNxGAuSfbBH3OcAf3x+BnnrOvqqCg7QCOOfqBPGMDOeOcffHsB1nV8WA8BpvbFXE+KL4bNY3muPqXm0oss3qMbHTXi+1EfqYyhFBQWmFmHvIIRFIwzkZI6fDT3wfLHGrzxaoucpw88lbLabNSOTkEbZqyC500YJViQZcKOUc5UuhU/ERqS/rINLWe3QWz1Xhp61QJXGxgMpS0HzluLBfqaNbgxRv1Ek4IfcO4WqTcqasud6qhFG4M9DRSGnM27B9ORYowQSoO1VKAHB9MthhE1LhS8mPhAA0jYSRc6Hyjfo64jiWmlNdAWJYmYjXgG14E9/LXV/BuK2dZ5b9YtMW6mjKvT0z3XU07xkgk1FTcKq0UMR2nLiT5lBngMOSjS9jfhX0gWOqtcV2paqFttRbaG50yRGUNllNv0bRfOKuRgpLWyOMgE8datdd3BVwiitmjbbu2qTV6nuNdXtLPtGXFOakupYljH6lU6qeWhXBUxZ1XrG7GVGqbzZaByGY0tHFJVtDyGKRfMGGKMBc4WEFUxhWIwTMTaB4dqDttBO/foTtiB6wgmpVZRsFVV5COKRrfXeMTRstw+HWxL6+jOzFFdJocmG5Xyz0NLGNgGxmuWr5Ki7IBkEtDRSEA5VSTjpB1X8TNTZIZae213bnRsRB20Fvpai/VZVTsTBj/AJHQiRCSQvyc6gBsIRnNeF31mksMhmv9xriHISjNXLTmUkcuYI8QpChwAmWkLABRxuAAtPV3qpY09GtOsgPp+uk7RszjgrMzAr7lnLHOTgHx1IUajSWq8Ci0Kna23edQdkEc8CLUgBw0zVYgEGpULCY3VNdLjiJ/XEhded99YX+60lVJ3a1VUR0dSZobPYaGKz2tGiLEmqgpFo47hGwUGSKoE9PLEzoyOhZXeDtz8WtpvEkNi1nsttV9ENPeNrxWuublB80cZtE+cHcztaycn1rdGFiaM1r+H+81NrF8utfSUlqB37kqIRvYjbsjaaWIbssBgKSp42knHXPS9sLRPdaahnvEVqtseDUXCpmSoaRFwH2Q07QRbgqkAzSwjeowXBOOPSy1VSCzMVFnEBliNwqhtrEGZO+Oh6qkEItME/DBCmwmxJ4YmZtE74sH1RaNP6tpXEqRTCVNyyDYZcOMqSxLCVGB3I4Y7l5jZVIbqC3dL4drbWioqbchWX6m3RqFUnyA424+2fUyN3AJwT079u1R2y7c2uCy2HWl81FVbkKU1QfmbfSt5dKaRKVUoICxJaCCqq0L/W0Mx3AuLadU2XU0Mr0dVT1EsSj5iD1EFTAGJCtUQgl4kfB2SkelIqncY2PprQNOtlzxICyWHFwnh2swuAdrz3E4tJVp1OwSOIT2JErpJHPwtblOKf7zpvuH2yrpqmxVFfShX3tBEWNLOAwyJ6WQPTuCowZNpkA/Q6nnpf038S8Sypb9fWWW2TA+m12t0bz0zc431FLj5mIDx/S+ZHG4hMdWb6p0PZr9TSiekgO4HGUUjJ9wfIJ8/QR7tg8ZhP3J+HOjqfWnoYAT9TCNVBOBz/TYBdxHgDAkHlsEDJ1fLVxFVQGMdpZBBEX3HgCCBtETjh66nejUMbow4lMQbCxFxqpFvDC1DqGyags719ludHcqSURuk1NMjjCSLvD4JKtG20SIVUowII3AgNbe9caYtMgpq280QqMlRSQzRzVIOOcwQlmUk4UmVYxnGSSQD86O0dHpHSl1oZYvVemus9TGsqDfGk9PSqYcOOf6sEjDjKs55OMmN9Ro+naSX16TZNI7yNKAyyiVmLswbIZSWLHKlQx5wRjJctl6ZaorOQivY2lgVUi9xprA19RV6tSKZ4BxlBxAkwpB0ECSL2uIjfDySaw/mEdbUW6ARpS07TpJVDcZiGVFUQwuNoJZfrMxwMYQ9Z029ioqy009fSyzvVR1EPpU7yKBJCGkhcrK4x6igRHY/p5Bbaxb9Yzq11agkArANiTBIgGfv5XwIOxAJkEgSORtPlPpPfi4qXWItFOlDbpKaiSkGz06Mp8zDCTkgiNmf0yDnYx2HADgMMAJrdc2qaWc12oGiTaW3bzPUM5PIWnhd1Dk5yGcY24KhuOmrq7Vda41FXeLqzCUhmpaCNKSlZFwB9EewEbDhSYgSAMYLHPAq2uhjZIaeGM4wzPukZsHnk7nOeM/4fcYwMiFJEi0kzPCI5bm8T5Ai+tzPVqMCAOEGCCxEwIBsDG+5nUYV7hqqSpnmp7elVLBJhIKmoY0iAscFmDIxJ3YYDaoXxg8ECdXQ3mkWaoqL4E+ciaCRYph6ppZdvqRF3IIjYYVlIBZSQcAsOtVdWS1MZjt0U0sp2hG9Fo4/II2kjOPyqknjaMZwgy2K7zsnz8zkk87ZFMi542gSNjd9WRuOPYjo6g7Qu9wCTpoJsO/nzjFR3X+pi5ETpANp015b6+eOaaez22J5IhBNUqRmVj60pdTk7AJBGuT+o7DjIycddLVF4r4ErYRLRrDgtPXTIkIAGU+XjUrIzAFSrBQmfGeD11LZbDYI1nutOlQZSxiWrqlkkDoVO8pGwj+g8Ko+nPkkgjpPq9ZWGgnWqiElfPEN0azf+U3JxGxDpkkDyv0jkMdx6mBOgLHmVBmIB5EeffGBGryBA5Ds3tc2MyI0juicKVPpzXd4Val4p56BMNHNc6uf5dQ3IdKUuzsp3ZQMPBBAAwelGG0UVE8kmoLxK0ighqSjLRIxB4RQj+qxAHIJjHJH6Rlgqu7vVU8fpxvDRxsoHpxtKzqx43O5OwtknwNoH1Y5yGrvGsUaokkWaSpqyQ5kWdv6XP/ALfq3ZGQq4wMcrnrqo7GOGJBPZiTpEbx38xaYGINEA8Qkm6yzW5k3FvXyvISn1NpqyrPHbrVbnm3k/P3TbtxuztZJTK7sh4wHj54JJBJB7l3ENhqTcbXXLS3FpGmElIEpIkkY8iOOAs8kbNgPGyCOQfr3AkBhauvu1WyyrOIlfO+T63lZT5BRzvznkH1BzgHIPW6NaK3hpq4y1LyKu5nYyyJgH9MZKxgAHH0KcYHH3MuXGsE8XxAySdBBnbugRsNTgbViQO0OwRHDCxEbqBceenjE0e3fxGxXWZbZq0LbquVlSmuEipBaKz2IMixK9rqDkbmqjPRTMHcy07ukXUiKqstc1MaiqqaeOEqDmqeKLhhlFBZism8cRmNmV/MZbHFQ9wvJIkSGoghjj5jMhIf0scBU3bQ5z7MwPjbjPRz24+IS+6CVaG6PNqTTRkCm2VdQqT0MZGHez1jAy0rKpP+xSF6OQFlVad5ZJhWr9EswNSgIa00iYmYsrEwD3MY2ECBixQ6TUQtaSLAVQCYmI4gILC92F+4mTiaurrbZburw0MdwpPUdHmuCU0MSAxNvUGCuQ1FQM8bZKeOORGbZVgAdMne+3NbsaWIUl0g5PqwRtDOoORmSDcxjP5jODj6Mjp7dM6z0p3DtP8ANtJ3BLpSKAayhcLBerNK4Y+jXUOfVQbgwSWLfS1G12glliUydbZaN03SxEMq+JISQyg/76r9SE/4iPoI+n9JPSriq0WKMCjKYZGBBBEazcWAjbQgYaDgqqGBDqwBDKZUgHaLa8535YiRVaVlpi2YXULu3JIPBBGdrkAgeThiu0eScdZ1JG5WCmuLu1wD1G/DRo2z5VFKgAtTwxxxVGWAYvULMyt/4Y4x1nRlryLkz/jv59wGtonEOqOwHLUg6jWPnqdNZ0WltMkzYqq5yoIDFAVjwckjOQcADg5JwQMDrnuFTpy2RPFNJFVSKhcxUyCeZQuCuTkpF45GATjHvjqPV517dKg7TJO4I4jDNBCAcgbo1I3AYByxJJGFHJboOa5XKYu5qDEpBLrETHI245HKgttAOSWYZPJHGemIpgaECL2ABt5zpa30OFRLNdnJJ0ubabC2ngPo9tf3Bp4RJ8hT0ySKAQJzvnwMYHoQMAgUYzvkwPBCt039211ebgJHWX0VBylPTBII0RRjh8tIM5O/DZwCCc+QJjPHLGDG08xbJBmTZySSG2s25sDLDAGT9yOuv0mqY3WRxCSWJSm4JwcHeqYJBxt5b6sDBIxmaqqkWBJ3vzE21OnO2ImAYAk2G0DSZJkA+nyONVReqlnCGUsarBbbI2F4wBIWdi24nG5mwf8ACMkZTK2KuATYQfciTBjIH5XDEkHbkjCg+RgZ2yRR0ewS5VWz9QjO4MMfSchsDHAPBBHPsRsn1BRx0/oVLbvSysTqP0gKQC+0FiwCjIVSGyMgEZ6KqtI4QeUG5vF+4nedI30wNqgEncR3C0eulhN9sJEdpaWdDVSCR5CB6ETCGFScDA348kAHLHJOFAJA67ZoKW2S/VDDGYscqodSWAOC2DgggjOVZSCc44IrV6h9SSQ006KqEhSVDHgHgjLbccblzuPH0g5wJ1NZXXCTajNOmM7lOEXPJGR9IAIBOATyMZI6uJQfchRG/K2m3dPha+K75hSYHE52ABIGnlJJNz3gxg4rbvTgmWOROWOMFdpGeQGBK8H/AAgH8qCT0EXC+yzTBCwkTcchDk4BwD/vAYI+kjnJxg460QUMZkzVPI4bgqjsigkeGblyD+ChH7DpUmpaKOILGkcLIuUKKMsPcNgBmB855PljxuBJ7umwEO5OhIMbDa+9zH64FLsJMKNxOumtwOW5H1HayZpT+l1BGAzgj38cjOSPYgYJyfPKfDSUzMGmXefb6jheSOAf2yPv44xjpytNaD1VrVkSw2aqq6aSUQNdJlemssDngercZE9J9rfSy0iVUqsVUopIPUtdEfCvYbaIa7WdWb/WriQ2qJWpbRE2clJYw5nrsZKN8zK1NNjPysTcdDr5/LZUdt5fanTkmZ3mAp0HaIPIHBqOSr14KIFW01Hsml4GreSkA6wcRO7dWvuDWXulrO3FLco66kl9H+b0n+yW+mO4erBX18i/ITQcA1FA61MkqEbqOUEDqzOy269y2W2vqZrbHqdacfzKoswlS2z1O45kgWYeoC0e0TEqoaQO3pJFshXuo7TS2OCOnstJS0NNEiRCghhSCjdEUKsZhiUBHVRiOZBvTABDpmMrEMsckZ27vvNTuT6sB9j9J+uMnhJU9sBtrBo1zmdzpzjg9UlML8JAmoRydyBI5KAAO/XD3KZQZVSOsdywEgmKY0uqXg21JJPdJwG1lGYyVqIfSkOSJIx/RkGeXAHCjgZZOBx6kcecdZ0WyorLsdfWgbna36k9w+5ceD4dSGGTkgnIzqlbcn5T9Ri4NB9dfPH/2Q==";


    public void createEventEdit(){
        mockEventEdit = new EventEditFragment();
    }
    // nothing to test currently.

    public void testConvertStringToBitmap() {

        byte[] bitmapArray;
        bitmapArray = Base64.getDecoder().decode(mockImageString);
        Bitmap image = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        //imageView.setImageBitmap(bitmap);
        assertEquals(image,mockEventEdit.stringToBitmap(mockImageString));
    }
    public void testConvertImageToString() {

        byte[] bitmapArray;
        bitmapArray = Base64.getDecoder().decode(mockImageString);
        Bitmap image = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        //imageView.setImageBitmap(bitmap);
        assertEquals(mockImageString,mockEventEdit.imageToString(image));
    }
}
