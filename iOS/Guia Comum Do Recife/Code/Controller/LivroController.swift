//
//  LivroController.swift
//  Guia Comum Do Recife
//
//  Created by Paula on 26/11/18.
//  Copyright © 2018 Paula Pithon. All rights reserved.
//

import UIKit
import AVFoundation
import CoreLocation
import MapKit

class LivroController: UIViewController {
    
    var mapa = Map()
    var count = 0
    var player: AVPlayer!
    
    @IBOutlet weak var previousBtn: UIButton!
    @IBOutlet weak var nextBtn: UIButton!
    
    @IBOutlet weak var audio: UIButton!
    @IBOutlet weak var label: UILabel!
    @IBOutlet weak var abstract: UITextView!
    @IBOutlet weak var more: UIButton!
    @IBOutlet weak var imageView: UIView!
    @IBOutlet weak var local: UIButton!
    @IBOutlet weak var share: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        loadButtons()
    }
    
    func loadButtons() {
        setBook()
        previousBtn.addTarget(self, action: #selector(previousBook), for: .touchUpInside)
        nextBtn.addTarget(self, action: #selector(nextBook), for: .touchUpInside)
    }
    
    @objc func nextBook(sender:UIButton) {
        if count + 1 < mapa.livros.count {
            count += 1
            setBook()
        }
    }
    
    @objc func previousBook(sender:UIButton) {
        if count - 1 >= 0 {
            count -= 1
            setBook()
        }
    }
    
    func setBook () {
        let livro = mapa.livros[count]
        
        //Set name and abstract
        label.text = livro.nome.uppercased()
        abstract.text = livro.resumo
        
        //Set main text
//        if livro.texto == "" {
            more.isHidden = true
//        } else {
//            more.isHidden = false
//        }
    
        //Load image
        if (livro.imagens.count > 0) {
            let image_url: URL = URL(string: livro.imagens[0])!
            let image_from_url_request: URLRequest = URLRequest(url: image_url)
            NSURLConnection.sendAsynchronousRequest(
                image_from_url_request, queue: OperationQueue.main,
                completionHandler: {(response: URLResponse!,
                    data: Data!,
                    error: Error!) -> Void in
                    
                    if error == nil && data != nil {
                        //Remove previous views
                        for view in self.imageView.subviews {
                            view.removeFromSuperview()
                        }
                        //Add new view
                        let image = UIImageView()
                        image.image = UIImage(data:data)
                        self.imageView.addSubview(image)
                        self.imageView.bringSubview(toFront: image)
                        image.translatesAutoresizingMaskIntoConstraints = false
                        NSLayoutConstraint.activate([
                            image.widthAnchor.constraint(equalTo: self.imageView.widthAnchor),
                            image.heightAnchor.constraint(equalTo: self.imageView.heightAnchor),
                            image.topAnchor.constraint(equalTo: self.imageView.topAnchor, constant: 0),
                            image.leadingAnchor.constraint(equalTo: self.imageView.leadingAnchor, constant: 0),
                            ])
                        image.contentMode = UIViewContentMode.scaleAspectFit
                    }
                    
            })
        }
        
        //Set audio button
        if (self.player != nil) { self.player.pause() }
        if livro.audio == "" {
            self.audio.isHidden = true
        } else {
            self.audio.isHidden = false
            self.audio.addTarget(self, action: #selector(playAudio), for: .touchUpInside)
        }
       
        //Set location
        if livro.local == "" {
            self.local.isHidden = true
        } else {
            self.local.isHidden = false
            self.local.addTarget(self, action: #selector(getLocation), for: .touchUpInside)
        }
        
        //Set share
        self.share.addTarget(self, action:#selector(shareMessage), for: .touchUpInside)
    }
    
    @objc func playAudio(sender:UIButton) {
        let url = URL.init(string: self.mapa.livros[count].audio)
        self.player = AVPlayer.init(url: url!)
        self.player.play()
    }
    
    @objc func getLocation(sender:UIButton) {
        let bookCoordinates = self.mapa.livros[count].local.components(separatedBy: ", ")
        let coordinates = CLLocationCoordinate2DMake(Double(bookCoordinates[0])!,Double(bookCoordinates[1])!)
        let regionSpan = MKCoordinateRegionMakeWithDistance(coordinates, 1000, 1000)
        let placemark = MKPlacemark(coordinate: coordinates, addressDictionary: nil)
        let mapItem = MKMapItem(placemark: placemark)
        mapItem.name = self.mapa.livros[count].nome
        mapItem.openInMaps(launchOptions:[
        MKLaunchOptionsMapCenterKey: NSValue(mkCoordinate: regionSpan.center)
        ] as [String : Any])
    }
    
    @objc func shareMessage(sender:UIButton) {
        let originalString = "Conheça o \(self.mapa.livros[count].nome) baixando o app Guia Comum do Recife!"
        let escapedString = originalString.addingPercentEncoding(withAllowedCharacters:CharacterSet.urlQueryAllowed)
        let url  = URL(string: "whatsapp://send?text=\(escapedString!)")
        if UIApplication.shared.canOpenURL(url! as URL) {
            UIApplication.shared.open(url! as URL, options: [:], completionHandler: nil)
        } else {
            let errorAlert = UIAlertView(title: "Impossível enviar mensagem", message: "Certifique-se que tem o Whatsapp instalado.", delegate: self, cancelButtonTitle: "OK")
            errorAlert.show()
        }
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if let vc = segue.destination as? DescriptionController {
            vc.mapa = mapa
            vc.count = count
        } else if let vc = segue.destination as? LivrosController {
            vc.mapa = mapa
        }
    }
 

}
