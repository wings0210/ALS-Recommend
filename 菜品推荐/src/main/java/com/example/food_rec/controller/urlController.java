package com.example.food_rec.controller;





import com.example.food_rec.Service.MainService;
import com.example.food_rec.domain.Storge;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
public class urlController {

    @GetMapping("/index")
    public String getindex(Model model)
    {


        String uid= Storge.getUserid();
        String[]c=new String[10];
        String[]b =MainService.get_url(c,"food","info","url",
                                         "user",uid,"info","recFoods",
                                                               "info","name");
        model.addAttribute("welcome",uid);

        model.addAttribute("image1",b[0]);
        model.addAttribute("image2",b[1]);
        model.addAttribute("image3",b[2]);
        model.addAttribute("image4",b[3]);
        model.addAttribute("image5",b[4]);
        model.addAttribute("image6",b[5]);
        model.addAttribute("image7",b[6]);
        model.addAttribute("image8",b[7]);
        model.addAttribute("image9",b[8]);
        model.addAttribute("image10",b[9]);

        model.addAttribute("rec1",c[0]);
        model.addAttribute("rec2",c[1]);
        model.addAttribute("rec3",c[2]);
        model.addAttribute("rec4",c[3]);
        model.addAttribute("rec5",c[4]);
        model.addAttribute("rec6",c[5]);
        model.addAttribute("rec7",c[6]);
        model.addAttribute("rec8",c[7]);
        model.addAttribute("rec9",c[8]);
        model.addAttribute("rec10",c[9]);




        return "index";
    }
}